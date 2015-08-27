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
    static final String FIELD_CHILDS = "child_id";
    static final String FIELD_PARENT = "parent_id";

    private static JSONArray fields = new JSONArray();

    static {
        fields.add(0, FIELD_ID);
        fields.add(1, FIELD_NAME);
        fields.add(2, FIELD_LOCATION);
        fields.add(3, FIELD_CODE);
        fields.add(4, FIELD_CHILDS);
        fields.add(5, FIELD_PARENT);
    }

    private JSONRPC2Session mJSONRPC2Session;
    private URL mSearchReadUrl;
    private String mJSONSessionId;
    private JSONObject mUsercontext;

    public CategoryClient(OpenERPConnector aOpenERPConnector , URL aSearchReadURL){
        mJSONRPC2Session = aOpenERPConnector.getOpenERPJsonSession();
        mSearchReadUrl = aSearchReadURL;
        mJSONSessionId = aOpenERPConnector.getOpenERPSessionId();
        mUsercontext = aOpenERPConnector.getOpenERPUserContext();
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
            Map<String, Object> categoryParams = new HashMap<>();
            categoryParams.put("session_id", mJSONSessionId);
            categoryParams.put("context", mUsercontext);
            categoryParams.put("domain", domain);
            categoryParams.put("model", "product.category");
            categoryParams.put("sort", "");
            categoryParams.put("fields", fields);

            jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, categoryParams, OpenERPUtil.generateRequestID()));
            categories = generateCategoryListFromJson(jsonRPC2Response);

        } catch (JSONRPC2SessionException e) {
            throw new OpenErpException(e.getMessage(), "");
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

            if((productJson.get(FIELD_LOCATION) instanceof Boolean)){
                anotherCategory.setLocation_id(0);
                anotherCategory.setLocationString(OpenERPConst.UNKNOW_LOCATION);
            }
            else{
                long location_id = (Long) ((JSONArray) productJson.get(FIELD_LOCATION)).get(0);
                String locationString = (String) (((JSONArray) productJson.get(FIELD_LOCATION)).get(1));
                anotherCategory.setLocation_id(location_id);
                anotherCategory.setLocationString(locationString);
            }


            long parent_id = 0;
            if(!(productJson.get(FIELD_PARENT) instanceof Boolean)){
                JSONArray parentArray = (JSONArray) productJson.get(FIELD_PARENT);
                if (parentArray.size() > 0) {
                    parent_id = (Long) parentArray.get(0);
                }
            }

            anotherCategory.setParent_category_id(parent_id);
            JSONArray childArray = (JSONArray) productJson.get(FIELD_CHILDS);
            List<Long> childIds = new ArrayList<>();
            if(childArray.size() > 0){
                for(int index = 0; index < childArray.size();index++){
                    childIds.add(new Long((long)childArray.get(index)));
                }
            }

            anotherCategory.setCategories(childIds);
            categories.add(anotherCategory);
        }
        return categories;
    }

    private String parseLocationString(String aLocationString){
        return  aLocationString.replaceAll("tatsächliche Lagerorte  / ", "");
    }

}
