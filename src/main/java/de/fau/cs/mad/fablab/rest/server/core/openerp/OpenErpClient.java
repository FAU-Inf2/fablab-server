package de.fau.cs.mad.fablab.rest.server.core.openerp;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.configuration.OpenErpConfiguration;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class OpenErpClient implements OpenErpInterface {

    private static OpenErpInterface instance;
    private static OpenErpConfiguration config = null;

    static final String REQUEST_AUTHENTICATE = "/web/session/authenticate";
    static final String REQUEST_SEARCH_READ = "/web/dataset/search_read";

    static final String METHOD = "call";

    static final String FIELD_CODE = "code";
    static final String FIELD_NAME = "name";
    static final String FIELD_RECEPTION_COUNT = "reception_count";
    static final String FIELD_VIRT_AVAILABLE = "virtual_available";
    static final String FIELD_LIST_PRICE = "lst_price";
    static final String FIELD_QTY_AVAILABLE = "qty_available";
    static final String FIELD_UNIT_OF_MEASURE = "uom_id";
    static final String FIELD_CATEGORY = "categ_id";
    static final String FIELD_LOCATION = "property_stock_location";


    private static JSONArray fields = new JSONArray();

    static {
        fields.add(0, FIELD_CODE);
        fields.add(1, FIELD_RECEPTION_COUNT);
        fields.add(2, FIELD_NAME);
        fields.add(3, FIELD_VIRT_AVAILABLE);
        fields.add(4, FIELD_LIST_PRICE);
        fields.add(5, FIELD_QTY_AVAILABLE);
        fields.add(6, FIELD_UNIT_OF_MEASURE);
        fields.add(7, FIELD_CATEGORY);
        fields.add(8, FIELD_LOCATION);
    }

    private URL mAuthenticateUrl;
    private URL mSearchReadUrl;
    private JSONRPC2Session mJsonSession;
    private JSONObject mUserContext;
    private String mSessionId;


    /***
     * Singleton getInstance()
     *
     * @return
     */
    public static OpenErpInterface getInstance() {
        if (instance == null) {
            try {
                instance = new OpenErpClient();
            } catch (MalformedURLException e) {
                System.err.println("ERROR - MalformedURLException while initializing OpenErpClient. \n" +
                                           "The Reason is : " + e.getMessage() + "\n" +
                                           "Your hostname is : " + config.getHostname());
                System.exit(1);
            }
        }
        return instance;
    }




    public static void setConfiguration(OpenErpConfiguration c) {
        config = c;
    }

    /***
     * Creates a jsonSession and gets a session id by calling authenticate()
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     *
     * @throws MalformedURLException if the hostname is not a valid url
     */
    private OpenErpClient() throws MalformedURLException {

        //If any of these variables is null, print an error message and exit(1)
        if (config == null || !config.validate()) {

            System.err.println("ERROR while initializing OpenErpClient. Configuration vars missing.\n" +
                                       "The configuration (username, password, hostname and database) has to be set \n " +
                                       "using the class OpenErpConfiguration.\n");
            System.exit(1);
        }

        mAuthenticateUrl = new URL(config.getHostname() + REQUEST_AUTHENTICATE);
        mSearchReadUrl = new URL(config.getHostname() + REQUEST_SEARCH_READ);
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
        authParams.put("db", config.getDatabase());
        authParams.put("login", config.getUsername());
        authParams.put("password", config.getPassword());

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
     * Requests a lists of all Categories as a tree
     *
     * @return List of {@link Category}
     * @throws OpenErpException
     */
    @Override
    public List<Category> getCategories() throws OpenErpException{
        List<Category> categories = new ArrayList<Category>();

        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(mSearchReadUrl);

            JSONArray domain = new JSONArray();
            JSONArray categoryFields = new JSONArray();
            categoryFields.add(0, "name");
            categoryFields.add(1, "id");
            categoryFields.add(2,"property_stock_location");

            Map<String, Object> categoryParams = new HashMap<>();
            categoryParams.put("session_id", mSessionId);
            categoryParams.put("context", mUserContext);
            categoryParams.put("domain", domain);
            categoryParams.put("model", "product.category");
            categoryParams.put("sort", "");
            categoryParams.put("fields", categoryFields);

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD, categoryParams, generateRequestID()));
            categories = generateCategoryListFromJson(jsonRPC2Response);

        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return categories;
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

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD, getProductParams(limit, offset, domain), generateRequestID()));

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
    public List<Product> searchForProductsByName(String searchString, int limit, int offset) throws OpenErpException {
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

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD, getProductParams(limit, offset, domain), generateRequestID()));

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


    @Override
    public List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(mSearchReadUrl);

            //the domain specific values to search
            JSONArray whereNameLike = new JSONArray();
            whereNameLike.add(0, "category_string");
            whereNameLike.add(1, "ilike");
            whereNameLike.add(2, searchString);


            JSONArray domain = new JSONArray();
            domain.add(0, whereNameLike);

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD, getProductParams(limit, offset, domain), generateRequestID()));

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
     * @param id a string containing 4 digits
     * @return
     * @throws OpenErpException
     */
    @Override
    public Product searchForProductsById(String id) throws OpenErpException {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(mSearchReadUrl);

            //the domain specific values to search
            JSONArray whereNameLike = new JSONArray();
            whereNameLike.add(0, "default_code");
            whereNameLike.add(1, "=");
            whereNameLike.add(2, id);


            JSONArray domain = new JSONArray();
            domain.add(0, whereNameLike);

            jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD, getProductParams(1, 0, domain), generateRequestID()));

            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJsonSession.setURL(mSearchReadUrl);
                jsonRPC2Response = mJsonSession.send(new JSONRPC2Request(METHOD, getProductParams(1, 0, domain), generateRequestID()));
            }
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response).get(0);
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

        List<Category> categories = getCategories();
        for (Object productObject : records) {

            JSONObject productJson = (JSONObject) productObject;

            String name = (productJson.get(FIELD_NAME) == null)
                          ? "unknown"
                          : (String) productJson.get(FIELD_NAME);

            //When there is no product_id available, the result is a bool value
            String id = (productJson.get(FIELD_CODE) instanceof Boolean)
                        ? ""
                        : (String) productJson.get(FIELD_CODE);

            Double price = (productJson.get(FIELD_LIST_PRICE) == null)
                           ? -1
                           : (Double) productJson.get(FIELD_LIST_PRICE);

            JSONArray categoryArray = (productJson.get(FIELD_CATEGORY) == null)
                                      ? new JSONArray()
                                      : (JSONArray) productJson.get(FIELD_CATEGORY);

            JSONArray unitOfMeasureArray = (productJson.get(FIELD_UNIT_OF_MEASURE) == null)
                                           ? new JSONArray()
                                           : (JSONArray) productJson.get(FIELD_UNIT_OF_MEASURE);

            String location = (productJson.get(FIELD_LOCATION) instanceof Boolean)
                              ? "unknown location"
                              : (String) ((JSONArray) productJson.get(FIELD_LOCATION)).get(1);

            String unit = "";
            if (unitOfMeasureArray.size() == 2) {
                unit = (String) unitOfMeasureArray.get(1);
            }

            long categoryId = -1;
            String categoryString = "unknown category";
            //ensure our array contains the needed elements
            if (categoryArray.size() == 2) {
                categoryId = (Long) categoryArray.get(0);
                categoryString = (String) categoryArray.get(1);
            }
            //Create a product and put it in the result list
            Product product = new Product(id, name, price, categoryId, categoryString, unit, location);
            product.setCategory(getCategoryObjectById(categories, categoryId));
            product.setLocation(product.getCategory().getLocation());
            productList.add(product);
        }
        return productList;
    }

    /**
     *
     * @param jsonResponse
     * @return A list contains {@link Category} elements
     * @throws OpenErpException
     */
    protected List<Category> generateCategoryListFromJson(JSONRPC2Response jsonResponse) throws OpenErpException{
        List<Category> categories = new ArrayList<>();
        JSONObject result = (JSONObject) jsonResponse.getResult();
        if (result == null) {
            throw new OpenErpException("JsonResponse contains no result", jsonResponse.getError().getMessage());
        }
        JSONArray records = (JSONArray) result.get("records");
        for(Object categoryObject : records) {
            JSONObject productJson = (JSONObject) categoryObject;
            Category anotherCategory = new Category();
            anotherCategory.setCategoryId((long) productJson.get("id"));
            anotherCategory.setName((String) productJson.get("name"));
            if((productJson.get("property_stock_location") instanceof Boolean)){
                anotherCategory.setLocation("unknown location");
            }
            else{
                anotherCategory.setLocation(parseLocationString((String)((JSONArray) productJson.get("property_stock_location")).get(1)));
            }
            categories.add(anotherCategory);
        }
        return categories;
    }

    protected Category getCategoryObjectById(List<Category> categories, long id){
        Category category = new Category();
        for(Category categ : categories){
            if(categ.getCategoryId() == id){
                category.setCategoryId(categ.getCategoryId());
                category.setName(categ.getName());
                category.setLocation(categ.getLocation());
            }
        }
        return category;
    }

    private String parseLocationString(String aLocationString){
        String newString =  aLocationString.replaceAll("tatsächliche Lagerorte  / ", "");
        return newString;
    }
}
