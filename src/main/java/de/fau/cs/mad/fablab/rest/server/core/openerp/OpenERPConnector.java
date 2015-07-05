package de.fau.cs.mad.fablab.rest.server.core.openerp;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.cs.mad.fablab.rest.server.configuration.OpenErpConfiguration;
import net.minidev.json.JSONObject;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OpenERPConnector {

    private String mAuthenticateRequest;
    private static OpenErpConfiguration config = null;
    private JSONRPC2Session mJsonSession;
    private URL mAuthenticateUrl;;
    private String mSessionId;
    private JSONObject mUserContext;

    private static final String ERP_DB_NAME = "db";
    private static final String ERP_LOGIN_NAME = "login";
    private static final String ERP_LOGIN_PW = "password";

    private static final String KEY_LANGUAGE = "lang";
    private static final String KEY_TIME_ZONE = "tz";
    private static final String KEY_USER_ID = "uid";
    private static final String KEY_SEARCH_FILTER = "search_default_filter_to_sell";

    private static final String VALUE_LANGUAGE = "de_DE";
    private static final String VALUE_TIME_ZONE = "Europe/Berlin";
    private static final String VALUE_USER_ID = "27";
    private static final String VALUE_SEARCH_FILTER = "1";

    private static final int REQUEST_CODE_SESSION_EXPIRED = 300;

    /**
     * @param aConfig
     * @param aAuthenticateRequest
     * @throws MalformedURLException
     */
    public OpenERPConnector(OpenErpConfiguration aConfig, String aAuthenticateRequest)  throws MalformedURLException {
        config = aConfig;
        mAuthenticateRequest = aAuthenticateRequest;
        mAuthenticateUrl = new URL(config.getHostname() + mAuthenticateRequest);
    }

    /***
     * Authenticates this JsonSession to the service.
     * This method has to be called before doing any requests to the service.
     */
    public void authenticate() {
        mJsonSession = new JSONRPC2Session(mAuthenticateUrl);
        mJsonSession.getOptions().acceptCookies(true);
        Map<String, Object> authParams = new HashMap<>();
        authParams.put(ERP_DB_NAME, config.getDatabase());
        authParams.put(ERP_LOGIN_NAME, config.getUsername());
        authParams.put(ERP_LOGIN_PW, config.getPassword());
        JSONRPC2Request authRequest = new JSONRPC2Request(OpenERPConst.METHOD_CALL, authParams, OpenERPUtil.generateRequestID());
        JSONRPC2Response jsonRPC2Response;
        try {
            jsonRPC2Response = mJsonSession.send(authRequest);
            mSessionId = ((JSONObject) jsonRPC2Response.getResult()).get("session_id").toString();
            //get the user_context from the connection result, to send to OpenERP in the next request
            mUserContext = getUserContext(jsonRPC2Response);
        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jsonrpc2Response
     * @return
     */
    private JSONObject getUserContext(JSONRPC2Response jsonrpc2Response){
        JSONObject userContext = new JSONObject();
        userContext = (JSONObject) ((JSONObject) jsonrpc2Response.getResult()).get("user_context");
        userContext.put(KEY_LANGUAGE, VALUE_LANGUAGE);
        userContext.put(KEY_TIME_ZONE, VALUE_TIME_ZONE);
        userContext.put(KEY_USER_ID, VALUE_USER_ID);
        userContext.put(KEY_SEARCH_FILTER, VALUE_SEARCH_FILTER);
        return userContext;
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
            if (response.getError().getCode() == REQUEST_CODE_SESSION_EXPIRED) {
                throw new OpenErpSessionExpiredException();

            }
        }
    }

    public String getOpenERPSessionId() {
        return mSessionId;
    }

    public JSONObject getOpenERPUserContext() {
        return mUserContext;
    }

    public JSONRPC2Session getOpenERPJsonSession() {
        return mJsonSession;
    }
}
