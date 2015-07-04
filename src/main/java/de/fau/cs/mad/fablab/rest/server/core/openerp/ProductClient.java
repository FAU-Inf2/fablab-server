package de.fau.cs.mad.fablab.rest.server.core.openerp;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import de.fau.cs.mad.fablab.rest.core.Product;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.URL;
import java.util.*;

public class ProductClient {

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

    private JSONRPC2Session mJSONRPC2Session;
    private URL mSearchReadUrl;
    private String mJSONSessionId;
    private JSONObject mUsercontext;

    public ProductClient(JSONRPC2Session aJsonSession, URL aSearchReadURL, String aJSONJsonrpc2SessionId,JSONObject aUsercontext){
        mJSONRPC2Session = aJsonSession;
        mSearchReadUrl = aSearchReadURL;
        mJSONSessionId = aJSONJsonrpc2SessionId;
        mUsercontext = aUsercontext;
    }


    public List<Product> getProducts(int limit, int offset) throws OpenErpException {
    JSONRPC2Response jsonRPC2Response = null;
    try {
        mJSONRPC2Session.setURL(mSearchReadUrl);

        JSONArray domain = new JSONArray();

        jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, getProductParams(limit, offset, domain), OpenERPUtil.generateRequestID()));

        try {
            assertSessionNotExpired(jsonRPC2Response);
        } catch (OpenErpSessionExpiredException e) {
            //do the request one more time.
            mJSONRPC2Session.setURL(mSearchReadUrl);
            jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD,
                    getProductParams(limit, offset, domain),
                    OpenERPUtil.generateRequestID()));

        }
    } catch (JSONRPC2SessionException e) {
        e.printStackTrace();
    }
    return generateProductListFromJson(jsonRPC2Response);
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

            long location_id = (productJson.get(FIELD_LOCATION) instanceof Boolean)
                    ? 0
                    : (Long) ((JSONArray) productJson.get(FIELD_LOCATION)).get(0);

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
            product.setLocation_id(location_id);
            productList.add(product);

        }
        return productList;
    }

    /***
     * searches for a substring inside product namesat the openerp web service
     *
     * @param searchString the substring to search for inside product names
     * @param limit        the maximum number of products to return
     * @param offset       the record offset
     * @return a List of {@link Product}
     */
    public List<Product> searchForProductsByName(String searchString, int limit, int offset) throws OpenErpException{
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJSONRPC2Session.setURL(mSearchReadUrl);

            //the domain specific values to search
            JSONArray whereNameLike = new JSONArray();
            whereNameLike.add(0, "name");
            whereNameLike.add(1, "ilike");
            whereNameLike.add(2, searchString);

            JSONArray whereDefaultCodeLike = new JSONArray();
            whereDefaultCodeLike.add(0, "default_code");
            whereDefaultCodeLike.add(1, "ilike");
            whereDefaultCodeLike.add(2, searchString);

            JSONArray filterSaleOk = new JSONArray();
            filterSaleOk.add(0, "sale_ok");
            filterSaleOk.add(1, "ilike");
            filterSaleOk.add(2, "true");

            JSONArray filterPrice = new JSONArray();
            filterPrice.add(0, "list_price");
            filterPrice.add(1, ">=");
            filterPrice.add(2, "0");


            JSONArray domain = new JSONArray();
            //add the filters to our given domain

            domain.add(0, "|");
            domain.add(1, whereNameLike);
            domain.add(2, whereDefaultCodeLike);
            domain.add(domain.size(), filterSaleOk);
            domain.add(domain.size(), filterPrice);

            JSONRPC2Request jsonrpc2Request = new JSONRPC2Request(METHOD, getProductParams(limit, offset, domain), OpenERPUtil.generateRequestID());
            jsonRPC2Response = mJSONRPC2Session.send(jsonrpc2Request);
            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJSONRPC2Session.setURL(mSearchReadUrl);
                jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD,
                        getProductParams(limit, offset, domain),
                        OpenERPUtil.generateRequestID()));
            }
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response);

    }


    public List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJSONRPC2Session.setURL(mSearchReadUrl);

            //the domain specific values to search
            JSONArray whereCategoryId = new JSONArray();
            whereCategoryId.add(0, "categ_id");
            whereCategoryId.add(1, "ilike");
            whereCategoryId.add(2, searchString);

            JSONArray filterSaleOk = new JSONArray();
            filterSaleOk.add(0, "sale_ok");
            filterSaleOk.add(1, "ilike");
            filterSaleOk.add(2, "true");

            JSONArray filterPrice = new JSONArray();
            filterPrice.add(0, "list_price");
            filterPrice.add(1, ">=");
            filterPrice.add(2, "0");

            JSONArray domain = new JSONArray();
            domain.add(0, "|");
            domain.add(1, whereCategoryId);
            domain.add(2, filterPrice);
            domain.add(3, whereCategoryId);

            JSONRPC2Request jsonrpc2Request = new JSONRPC2Request(METHOD, getProductParams(limit, offset, domain), OpenERPUtil.generateRequestID());
            jsonRPC2Response = mJSONRPC2Session.send(jsonrpc2Request);

            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJSONRPC2Session.setURL(mSearchReadUrl);
                jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD,
                        getProductParams(limit, offset, domain),
                        OpenERPUtil.generateRequestID()));
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
    public Product searchForProductsById(String id) throws OpenErpException {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJSONRPC2Session.setURL(mSearchReadUrl);

            //the domain specific values to search
            JSONArray whereNameLike = new JSONArray();
            whereNameLike.add(0, "default_code");
            whereNameLike.add(1, "=");
            whereNameLike.add(2, id);


            JSONArray domain = new JSONArray();
            domain.add(0, whereNameLike);

            jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, getProductParams(1, 0, domain), OpenERPUtil.generateRequestID()));

            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJSONRPC2Session.setURL(mSearchReadUrl);
                jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, getProductParams(1, 0, domain), OpenERPUtil.generateRequestID()));
            }
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return generateProductListFromJson(jsonRPC2Response).get(0);
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
                // authenticate();
                //notify the caller about the expired session
                throw new OpenErpSessionExpiredException();
            }
        }
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

        //we only want products marked as sale_ok and a price >= 0
        JSONArray filterSaleOk = new JSONArray();
        filterSaleOk.add(0, "sale_ok");
        filterSaleOk.add(1, "ilike");
        filterSaleOk.add(2, "true");

        JSONArray filterPrice = new JSONArray();
        filterPrice.add(0, "list_price");
        filterPrice.add(1, ">=");
        filterPrice.add(2, "0");

        //add the filters to our given domain
        //domain.add(domain.size(), filterSaleOk);
        //domain.add(domain.size(), filterPrice);

        Map<String, Object> productParams = new HashMap<>();
        productParams.put("session_id", mJSONSessionId);
        productParams.put("context", mUsercontext);
        productParams.put("domain", domain);
        productParams.put("model", "product.product");
        productParams.put("limit", limit);
        productParams.put("sort", "");
        productParams.put("offset", offset);
        productParams.put("fields", fields);
        return productParams;
    }
}
