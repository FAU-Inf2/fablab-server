package de.fau.cs.mad.fablab.rest.server.openerp;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.core.Product;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class OpenErpClient implements OpenErpInterface {

    static OpenErpInterface instance;

    //Keys for the environment variables containing the data for openerp access
    static final String OPENERP_HOST_KEY = "openerp_hostname";
    static final String OPENERP_DATABASE_KEY = "openerp_database";
    static final String OPENERP_USER_KEY = "openerp_user";
    static final String OPENERP_PASSWORD_KEY = "openerp_password";

    static final String REQUEST_AUTHENTICATE = "/web/session/authenticate";
    static final String REQUEST_SEARCH_READ = "/web/dataset/search_read";
    static final String METHOD = "call";

    private static JSONArray fields = new JSONArray();

    static {
        fields.add(0, "code");
        fields.add(1, "reception_count");
        fields.add(2, "name");
        fields.add(3, "virtual_available");
        fields.add(4, "lst_price");
        fields.add(5, "list_price");
        fields.add(6, "color");
        fields.add(7, "qty_available");
        fields.add(8, "type");
        fields.add(9, "uom_id");
        fields.add(10, "delivery_count");
        fields.add(11, "__last_update");
        fields.add(12, "categ_id");
    }

    private URL mAuthenticateUrl;
    private URL mSearchReadUrl;
    private String mHostname;
    private String mUser;
    private String mPassword;
    private String mDatabase;
    private JSONRPC2Session mJsonSession;
    private JSONObject mUserContext;
    private String mSessionId;


    /***
     * Singleton getInstance()
     * @return
     */
    public static OpenErpInterface getInstance(){
        if(instance == null){
            try {
                instance = new OpenErpClient();
            } catch (MalformedURLException e) {
                System.err.println("ERROR - MalformedURLException while initializing OpenErpClient. \n" +
                                           "The Reason is : "+e.getMessage()+"\n"+
                                           "Your hostname is : "+System.getenv().get(OPENERP_HOST_KEY));
                System.exit(1);
            }
        }
        return instance;
    }

    /***
     * Creates a jsonSession and gets a session id by calling authenticate()
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     *
     * @throws MalformedURLException if the hostname is not a valid url
     */
    private OpenErpClient() throws MalformedURLException {

        mHostname = System.getenv().get(OPENERP_HOST_KEY);
        mUser = System.getenv().get(OPENERP_USER_KEY);
        mPassword = System.getenv().get(OPENERP_PASSWORD_KEY);
        mDatabase = System.getenv().get(OPENERP_DATABASE_KEY);

        //If any of these variables is null, print an error message and exit(1)
        if (mHostname == null || mUser == null || mPassword == null || mDatabase == null) {

            System.err.println("ERROR while initializing OpenErpClient. Environment vars missing.\n" +
                                       "the following variables have to be defined :\n" +
                                       "\t" + OPENERP_HOST_KEY + "=\"openerp hostname\"\n" +
                                       "\t" + OPENERP_DATABASE_KEY + "=\"openerp database name\"\n" +
                                       "\t" + OPENERP_USER_KEY + "=\"openerp username\"\n" +
                                       "\t" + OPENERP_PASSWORD_KEY + "=\"openerp user password\"\n");
            System.exit(1);
        }

        mAuthenticateUrl = new URL(mHostname + REQUEST_AUTHENTICATE);
        mSearchReadUrl = new URL(mHostname + REQUEST_SEARCH_READ);
        authenticate();
    }

    /***
     * Authenticates this JsonSession to the service.
     * This method has to be called before doing any requests to the service.
     */
    private void authenticate() {

        mJsonSession = new JSONRPC2Session(mAuthenticateUrl);
        mJsonSession.getOptions().acceptCookies(true);

        Map<String, Object> authParams = new HashMap<>();
        authParams.put("db", mDatabase);
        authParams.put("login", mUser);
        authParams.put("password", mPassword);

        JSONRPC2Request authRequest = new JSONRPC2Request(METHOD, authParams, generateRequestID());
        JSONRPC2Response jsonRPC2Response;

        try {
            jsonRPC2Response = mJsonSession.send(authRequest);
            mSessionId = ((JSONObject) jsonRPC2Response.getResult()).get("session_id").toString();

            //get the user_context from the connection result, to send to OpenERP in the next request
            mUserContext = (JSONObject) ((JSONObject) jsonRPC2Response.getResult()).get("user_context");
            mUserContext.put("lang", "de_DE");
            mUserContext.put("tz", "Europe/Berlin");
            mUserContext.put("uid", "27");
            mUserContext.put("search_default_filter_to_sell", "1");

        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a random request id
     *
     * @return
     */
    private String generateRequestID() {
        return "rid" + new Random().nextInt(Integer.MAX_VALUE);
    }

    /***
     * Requests a list of products at the openerp web service
     *
     * @param limit  the maximum number of products to return
     * @param offset the record offset
     * @return a List of {@link Product}
     */
    @Override
    public List<Product> getProducts(int limit, int offset) throws OpenErpException {

        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(mSearchReadUrl);

            JSONArray domain = new JSONArray();

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD,
                                                                     getProductParams(limit, offset, domain),
                                                                     generateRequestID()));

            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJsonSession.setURL(mSearchReadUrl);
                jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD,
                                                                         getProductParams(limit, offset, domain),
                                                                         generateRequestID()));
            }

        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response);
    }

    /***
     * searches for a substring inside product namesat the openerp web service
     *
     * @param searchString the substring to search for inside product names
     * @param limit        the maximum number of products to return
     * @param offset       the record offset
     * @return a List of {@link Product}
     */
    @Override
    public List<Product> searchForProducts(String searchString, int limit, int offset) throws OpenErpException {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(mSearchReadUrl);

            //the domain specific values to search
            JSONArray whereNameLike = new JSONArray();
            whereNameLike.add(0, "name");
            whereNameLike.add(1, "ilike");
            whereNameLike.add(2, searchString);

            JSONArray whereDefaultCodeLike = new JSONArray();
            whereDefaultCodeLike.add(0, "default_code");
            whereDefaultCodeLike.add(1, "ilike");
            whereDefaultCodeLike.add(2, searchString);

            JSONArray domain = new JSONArray();
            domain.add(0, "|");
            domain.add(1, whereNameLike);
            domain.add(2, whereDefaultCodeLike);

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD,
                                                                     getProductParams(limit, offset, domain),
                                                                     generateRequestID()));

            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJsonSession.setURL(mSearchReadUrl);
                jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD,
                                                                         getProductParams(limit, offset, domain),
                                                                         generateRequestID()));
            }
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response);
    }

    /***
     * Builds a HashMap containing parameters used for a product.product search-read
     *
     * @param limit  the max count of records which should be returned
     * @param offset the offset to start the records
     * @param domain additional, domain specific parameters, given as {@link JSONArray}
     * @return
     */
    private Map<String, Object> getProductParams(int limit, int offset, JSONArray domain) {

        Map<String, Object> productParams = new HashMap<>();
        productParams.put("session_id", mSessionId);
        productParams.put("context", mUserContext);
        productParams.put("domain", domain);
        productParams.put("model", "product.product");
        productParams.put("limit", limit);
        productParams.put("sort", "");
        productParams.put("offset", offset);
        productParams.put("fields", fields);

        return productParams;
    }

    /***
     * Checks if a given {@link JSONRPC2Response} contains an error code 300
     * if so, it might be an expired session.
     * If the param contains an error 300, this method re-authenticates the client and throws an
     * {@link OpenErpSessionExpiredException}
     *
     * @param response the {@link JSONRPC2Response} to check
     * @throws OpenErpSessionExpiredException when response contains error code 300
     */
    private void assertSessionNotExpired(JSONRPC2Response response) throws OpenErpSessionExpiredException {
        if (response.getError() != null) {
            if (response.getError().getCode() == 300) {
                //instantly try to re-authenticate at the service
                authenticate();
                //notify the caller about the expired session
                throw new OpenErpSessionExpiredException();
            }
        }
    }

    /**
     * Parses a {@link JSONRPC2Response} object and creates a {@link Product} element for each
     * entry
     *
     * @param jsonResponse the {@link JSONRPC2Response} object to parse
     * @return a List containing {@link Product} elements
     * @throws OpenErpException if jsonResponse contains no result
     */
    protected List<Product> generateProductListFromJson(JSONRPC2Response jsonResponse) throws OpenErpException {
        List<Product> productList = new LinkedList<>();

        JSONObject result = (JSONObject) jsonResponse.getResult();
        if (result == null) {
            //something went wrong;
            throw new OpenErpException("JsonResponse contains no result", jsonResponse.getError().getMessage());
        }
        JSONArray records = (JSONArray) result.get("records");

        for (Object productObject : records) {
            JSONObject productJson = (JSONObject) productObject;

            String name = (productJson.get("name") == null)
                          ? "unknown"
                          : (String) productJson.get("name");

            Long id = (productJson.get("id") == null)
                      ? -1
                      : (Long) productJson.get("id");

            Double price = (productJson.get("list_price") == null)
                           ? -1
                           : (Double) productJson.get("list_price");

            JSONArray categoryArray = (productJson.get("categ_id") == null)
                                      ? new JSONArray()
                                      : (JSONArray) productJson.get("categ_id");

            long categoryId = -1;
            String categoryString = "unknown category";
            //ensure our array contains the needed elements
            if (categoryArray.size() == 2) {
                categoryId = (Long) categoryArray.get(0);
                categoryString = (String) categoryArray.get(1);
            }
            //Create a product and put it in the result list
            //TODO what is unit here? (last param in the Product Constructor not used right now)
            Product product = new Product(id, name, price, categoryId, categoryString, "");
            productList.add(product);
        }
        return productList;
    }
}
