package de.fau.cs.mad.fablab.rest.server.core.openerp;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
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
        CategoryClient categoryClient = new CategoryClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        return categoryClient.getCategories();
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
        CategoryClient categoryClient = new CategoryClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        LocationClient locationClient = new LocationClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        List<Category> categories = categoryClient.getCategories();
        List<Location> locations = locationClient.getLocations();


        ProductClient productClient = new ProductClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        List<Product> products = productClient.getProducts(limit,offset);

        for(Product product : products){
            product.setCategory(getCategoryObjectById(categories, product.getCategoryId()));
            product.setLocationObject(getLocationById(locations, product.getLocation_id()));
        }

        return products;
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
        ProductClient productClient = new ProductClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        return productClient.searchForProductsByName(searchString, limit, offset);
    }


    @Override
    public List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException {
        ProductClient productClient = new ProductClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        return productClient.searchForProductsByCategory(searchString, limit, offset);
    }

    /***
     * @param id a string containing 4 digits
     * @return
     * @throws OpenErpException
     */
    @Override
    public Product searchForProductsById(String id) throws OpenErpException {
        ProductClient productClient = new ProductClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        Product product = productClient.searchForProductsById(id);

        CategoryClient categoryClient = new CategoryClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        LocationClient locationClient = new LocationClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        List<Category> categories = categoryClient.getCategories();
        List<Location> locations = locationClient.getLocations();

        product.setCategory(getCategoryObjectById(categories, product.getCategoryId()));
        product.setLocationObject(getLocationById(locations, product.getLocation_id()));
        return product;
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

    public List<Location> getLocations(){
        LocationClient locationClient = new LocationClient(mJsonSession,mSearchReadUrl,mSessionId,mUserContext);
        return locationClient.getLocations();
    }

    private Location getLocationById(final List<Location> aLocations,final long aLocationId){
        Location newLocation = new Location();
        for(Location location : aLocations){
            if(location.getId() == aLocationId){
                System.out.println("found location with id: " + aLocationId);
                newLocation.setId(location.getId());
                newLocation.setName(location.getName());
                newLocation.setCode(location.getCode());
                return newLocation;
            }
        }
        return newLocation;
    }

    private Category getCategoryObjectById(List<Category> categories, long id) {
        Category category = new Category();
        for (Category categ : categories) {
            if (categ.getCategoryId() == id) {
                category.setCategoryId(categ.getCategoryId());
                category.setName(categ.getName());
                category.setLocation_id(categ.getLocation_id());
            }
        }
        return category;

    }
}
