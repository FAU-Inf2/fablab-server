package de.fau.cs.mad.fablab.rest.server.openerp;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.entities.Product;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class OpenErpClient implements OpenErpInterface {

    static final String REQUEST_AUTHENTICATE = "/web/session/authenticate";
    static final String REQUEST_SEARCH_READ = "/web/dataset/search_read";
    static final String METHOD = "call";

    private String mHostname;
    private String mUser;
    private String mPassword;
    private String mDatabase;
    private JSONRPC2Session mJsonSession;

    private JSONObject mUserContext;
    private String mSessionId;

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

    public OpenErpClient(String host, String database, String password, String user) {
        mHostname = host;
        mUser = user;
        mPassword = password;
        mDatabase = database;
    }

    @Override
    public void authenticate() throws MalformedURLException {

        mJsonSession = new JSONRPC2Session(new URL(mHostname + REQUEST_AUTHENTICATE));
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

    @Override
    public List<Product> getProducts(int limit, int offset) {

        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(new URL(mHostname + REQUEST_SEARCH_READ));

            JSONArray domain = new JSONArray();

            Map<String, Object> productParams = new HashMap<String, Object>();
            productParams.put("session_id", mSessionId);
            productParams.put("context", mUserContext);
            productParams.put("domain", domain);
            productParams.put("model", "product.product");
            productParams.put("limit", limit);
            productParams.put("sort", "");
            productParams.put("offset", offset);
            productParams.put("fields", fields);

            JSONRPC2Request productRequest = new JSONRPC2Request(METHOD, productParams, generateRequestID());
            System.out.println("Request of all products: " + productRequest);
            jsonRPC2Response = mJsonSession.send(productRequest);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response);
    }

    @Override
    public List<Product> searchForProducts(String searchString, int maxResults, int offset) {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(new URL(mHostname + REQUEST_SEARCH_READ));

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

            Map<String, Object> productParams = new HashMap<String, Object>();
            productParams.put("session_id", mSessionId);
            productParams.put("context", mUserContext);
            productParams.put("domain", domain);
            productParams.put("model", "product.product");
            productParams.put("limit", maxResults);
            productParams.put("sort", "");
            productParams.put("offset", offset);
            productParams.put("fields", fields);

            JSONRPC2Request searchRequest = new JSONRPC2Request(METHOD, productParams, generateRequestID());
            System.out.println("Request of search for name: " + searchRequest);
            jsonRPC2Response = mJsonSession.send(searchRequest);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response);
    }

    /**
     * Generate a random request id
     *
     * @return
     */
    private String generateRequestID() {
        return "rid" + new Random().nextInt(Integer.MAX_VALUE);
    }

    /**
     * Parses a {@link JSONRPC2Response} object and creates a {@link Product} element for each
     * entry
     *
     * @param jsonResponse the {@link JSONRPC2Response} object to parse
     * @return a List containing {@link Product} elements
     */
    protected List<Product> generateProductListFromJson(JSONRPC2Response jsonResponse) {
        List<Product> productList = new LinkedList<>();

        JSONObject result = (JSONObject) jsonResponse.getResult();
        JSONArray records = (JSONArray) result.get("records");

        for (Object productObject : records) {
            JSONObject productJson = (JSONObject) productObject;
            String name = (String) productJson.getOrDefault("name", "unknown");
            Long id = (Long) productJson.getOrDefault("id", "-1");
            Double price = (Double) productJson.getOrDefault("list_price", "-1");
            JSONArray categoryArray = (JSONArray) productJson.getOrDefault("categ_id", new JSONArray());

            long categoryId = -1;
            String categoryString = "unknown category";
            //ensure our array contains the needed elements
            if (categoryArray.size() == 2) {
                categoryId = (Long) categoryArray.get(0);
                categoryString = (String) categoryArray.get(1);
            }
            //Create a product and put it in the result list
            Product product = new Product(id, name, price, categoryId, categoryString);
            productList.add(product);
        }
        for (Product tmp : productList) {
            System.out.println(tmp.toString());
        }
        return productList;
    }
}
