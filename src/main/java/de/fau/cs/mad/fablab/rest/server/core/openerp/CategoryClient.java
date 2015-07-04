package de.fau.cs.mad.fablab.rest.server.core.openerp;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.URL;
import java.util.*;

public class CategoryClient {

    static final String METHOD = "call";
    static final String FIELD_ID = "id";
    static final String FIELD_CODE = "code";
    static final String FIELD_NAME = "name";
    static final String FIELD_LOCATION = "property_stock_location";


    private static JSONArray fields = new JSONArray();

    static {
        fields.add(0, FIELD_ID);
        fields.add(1, FIELD_NAME);
        fields.add(2, FIELD_LOCATION);
        fields.add(3, FIELD_CODE);
    }


    private JSONRPC2Session mJSONRPC2Session;
    private URL mSearchReadUrl;
    private String mJSONSessionId;
    private JSONObject mUsercontext;

    public CategoryClient(JSONRPC2Session aJsonSession, URL aSearchReadURL, String aJSONJsonrpc2SessionId,JSONObject aUsercontext){
        mJSONRPC2Session = aJsonSession;
        mSearchReadUrl = aSearchReadURL;
        mJSONSessionId = aJSONJsonrpc2SessionId;
        mUsercontext = aUsercontext;
    }

    /***
     * Requests a lists of all Categories as a tree
     *
     * @return List of {@link Category}
     * @throws OpenErpException
     */
    public List<Category> getCategories() throws OpenErpException{
        List<Category> categories = new ArrayList<Category>();

        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJSONRPC2Session.setURL(mSearchReadUrl);

            JSONArray domain = new JSONArray();
            JSONArray categoryFields = new JSONArray();
            categoryFields.add(0,"name");
            categoryFields.add(1,"id");
            categoryFields.add(2,"property_stock_location");


            Map<String, Object> categoryParams = new HashMap<>();
            categoryParams.put("session_id", mJSONSessionId);
            categoryParams.put("context", mUsercontext);
            categoryParams.put("domain", domain);
            categoryParams.put("model", "product.category");
            categoryParams.put("sort", "");
            categoryParams.put("fields", categoryFields);

            jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, categoryParams, OpenERPUtil.generateRequestID()));
            categories = generateCategoryListFromJson(jsonRPC2Response);

        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
        return categories;
    }

    private List<Category> generateCategoryListFromJson(JSONRPC2Response jsonResponse) throws OpenErpException{
        List<Category> categories = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
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
                anotherCategory.setLocation_id(0);
            }
            else{
                long location_id = (Long) ((JSONArray) productJson.get("property_stock_location")).get(0);
                anotherCategory.setLocation_id(location_id);
            }
            categories.add(anotherCategory);
        }
        return categories;
    }


    private String parseLocationString(String aLocationString){
        String newString =  aLocationString.replaceAll("tatsächliche Lagerorte  / ", "");
        return newString;
    }

}
