package de.fau.cs.mad.fablab.rest.server.core.openerp;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.core.UOM;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.URL;
import java.util.*;

public class UOMClient {

    static final String METHOD = "call";
    static final String FIELD_UOM_ID = "id";
    static final String FIELD_NAME = "name";
    static final String FIELD_FACTOR_INV = "factor_inv";
    static final String FIELD_ROUNDING = "rounding";
    static final String FIELD_ACTIVE = "active";
    static final String FIELD_FACTOR = "factor";
    static final String FIELD_UOM_TYPE = "uom_type";
    static final String FIELD_CATEGORY_ID = "category_id";


    private static JSONArray fields = new JSONArray();
    static {
        fields.add(0, FIELD_UOM_ID);
        fields.add(1, FIELD_NAME);
        fields.add(2, FIELD_FACTOR_INV);
        fields.add(3, FIELD_ROUNDING);
        fields.add(4, FIELD_ACTIVE);
        fields.add(5, FIELD_FACTOR);
        fields.add(6, FIELD_UOM_TYPE);
        fields.add(7, FIELD_CATEGORY_ID);
    }

    private JSONRPC2Session mJSONRPC2Session;
    private URL mSearchReadUrl;
    private String mJSONSessionId;
    private JSONObject mUsercontext;

    public UOMClient(OpenERPConnector aOpenERPConnector , URL aSearchReadURL){
        mJSONRPC2Session = aOpenERPConnector.getOpenERPJsonSession();
        mSearchReadUrl = aSearchReadURL;
        mJSONSessionId = aOpenERPConnector.getOpenERPSessionId();
        mUsercontext = aOpenERPConnector.getOpenERPUserContext();
    }

    public List<UOM> getUOMs() throws OpenErpException {
        List<UOM> uoms = new ArrayList<>();
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJSONRPC2Session.setURL(mSearchReadUrl);
            // emtpy domain
            JSONArray domain = new JSONArray();

            JSONRPC2Request jsonrpc2Request = new JSONRPC2Request(METHOD, getUOMParams(domain), OpenERPUtil.generateRequestID());
            jsonRPC2Response = mJSONRPC2Session.send(jsonrpc2Request);

            try {
                assertSessionNotExpired(jsonRPC2Response);
            } catch (OpenErpSessionExpiredException e) {
                //do the request one more time.
                mJSONRPC2Session.setURL(mSearchReadUrl);
                jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, getUOMParams(domain), OpenERPUtil.generateRequestID()));
            }
        }catch (JSONRPC2SessionException e){
            throw new OpenErpException(e.getMessage(), e.getCause().toString());
        }
        return generateOUMsFromJson(jsonRPC2Response);
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
                throw new OpenErpSessionExpiredException();
            }
        }
    }

    private List<UOM> generateOUMsFromJson(JSONRPC2Response jsonResponse) throws OpenErpException{
        List<UOM> uoms = new LinkedList<>();
        JSONObject result = (JSONObject) jsonResponse.getResult();
        if(result == null){
            throw new OpenErpException("JsonResponse contains no result", jsonResponse.getError().getMessage());
        }
        JSONArray records = (JSONArray) result.get("records");

        for(Object uomObject : records){
            UOM uom = new UOM();
            JSONObject productJson = (JSONObject) uomObject;
            uom.setUom_id((Long) productJson.get(FIELD_UOM_ID));
            uom.setName((String) productJson.get(FIELD_NAME));
            uom.setRounding((Double) productJson.get(FIELD_ROUNDING));
            uom.setUomType((String) productJson.get(FIELD_UOM_TYPE));
            uoms.add(uom);
        }
        return uoms;
    }

    /***
     * Builds a HashMap containing parameters used for a product.product search-read
     *
     * @param domain additional, domain specific parameters, given as {@link JSONArray}
     * @return
     */
    private Map<String, Object> getUOMParams(JSONArray domain) {
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("session_id", mJSONSessionId);
        productParams.put("context", mUsercontext);
        productParams.put("domain", domain);
        productParams.put("model", "product.uom");
        productParams.put("sort", "");
        productParams.put("fields", fields);
        return productParams;
    }

}
