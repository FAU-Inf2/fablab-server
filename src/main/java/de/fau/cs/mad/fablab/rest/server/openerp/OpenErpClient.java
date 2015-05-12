package de.fau.cs.mad.fablab.rest.server.openerp;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OpenErpClient implements  OpenErpInterface {
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

    public OpenErpClient(String host, String database, String password, String user)
    {
        mHostname = host;
        mUser = user;
        mPassword = password;
        mDatabase = database;
    }

    @Override
    public void authenticate() throws MalformedURLException {
        mJsonSession = new JSONRPC2Session(new URL(mHostname + REQUEST_AUTHENTICATE));
        mJsonSession.getOptions().acceptCookies(true);
        mJsonSession.getOptions().trustAllCerts(true);

        Map<String,Object> authParams = new HashMap<String,Object>();
        authParams.put("db", mDatabase);
        authParams.put("login", mUser);
        authParams.put("password", mPassword);

        JSONRPC2Request authRequest = new JSONRPC2Request(METHOD, authParams, generateRequestID());
        JSONRPC2Response jsonRPC2Response = null;

        try {
            jsonRPC2Response = mJsonSession.send(authRequest);
            mSessionId = ((JSONObject)jsonRPC2Response.getResult()).get("session_id").toString();

            //get the user_context from the connection result, to send to OpenERP in the next request
            mUserContext = (JSONObject)((JSONObject)jsonRPC2Response.getResult()).get("user_context");
            mUserContext.put("lang", "de_DE");
            mUserContext.put("tz", "Europe/Berlin");
            mUserContext.put("uid", "27");
            mUserContext.put("search_default_filter_to_sell", "1");

            //Session id in cookie here?
            /*for(HttpCookie cookie : mJsonSession.getCookies())
            {
                System.out.println("Cookie Name : "+cookie.getName());
                System.out.println("Cookie URL : "+cookie.getDomain());
                System.out.println("Cookie Path : "+cookie.getPath());
                System.out.println("Cookie Comment : "+cookie.getComment());
                System.out.println("Cookie Value : "+cookie.getValue());
            }
            */

        } catch (JSONRPC2SessionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public JSONRPC2Response getProducts(int limit, int offset) {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(new URL(mHostname + REQUEST_SEARCH_READ));

            JSONArray domain = new JSONArray();
            JSONArray whereSaleOk = new JSONArray();
            whereSaleOk.add(0, "sale_ok");
            whereSaleOk.add(1, "=");
            whereSaleOk.add(2, "1");
            domain.add(0, whereSaleOk);

            JSONArray fields = new JSONArray();
            fields.add(0, "code");
            fields.add(1,"reception_count");
            fields.add(2, "name");
            fields.add(3,"virtual_available");
            fields.add(4,"lst_price");
            fields.add(5,"list_price");
            fields.add(6,"color");
            fields.add(7,"qty_available");
            fields.add(8,"type");
            fields.add(9, "uom_id");
            fields.add(10, "delivery_count");
            fields.add(11, "__last_update");

            Map<String,Object> productParams = new HashMap<String,Object>();
            productParams.put("session_id", mSessionId);
            productParams.put("context", mUserContext);
            productParams.put("domain",domain);
            productParams.put("model", "product.product");
            productParams.put("limit", limit);
            productParams.put("sort","");
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
        return jsonRPC2Response;
    }

    @Override
    public JSONRPC2Response searchForProducts(final int limit, final int offset, final String aValue) {
        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJsonSession.setURL(new URL(mHostname + REQUEST_SEARCH_READ));

            JSONArray domain = new JSONArray();

            JSONArray whereSaleOk = new JSONArray();
            whereSaleOk.add(0, "sale_ok");
            whereSaleOk.add(1, "=");
            whereSaleOk.add(2, "1");

            JSONArray whereNameLike = new JSONArray();
            whereNameLike.add(0,"name");
            whereNameLike.add(1,"ilike");
            whereNameLike.add(2,aValue);

            JSONArray whereDefaultCodeLike = new JSONArray();
            whereDefaultCodeLike.add(0,"default_code");
            whereDefaultCodeLike.add(1,"ilike");
            whereDefaultCodeLike.add(2,aValue);

            domain.add(0,"|");
            //domain.add(1, whereSaleOk);
            domain.add(1,whereNameLike);
            domain.add(2,whereDefaultCodeLike);



            JSONArray fields = new JSONArray();
            fields.add(0, "code");
            fields.add(1,"reception_count");
            fields.add(2, "name");
            fields.add(3,"virtual_available");
            fields.add(4,"lst_price");
            fields.add(5,"list_price");
            fields.add(6,"color");
            fields.add(7,"qty_available");
            fields.add(8,"type");
            fields.add(9, "uom_id");
            fields.add(10, "delivery_count");
            fields.add(11, "__last_update");


            Map<String,Object> productParams = new HashMap<String,Object>();
            productParams.put("session_id", mSessionId);
            productParams.put("context", mUserContext);
            productParams.put("domain",domain);
            productParams.put("model", "product.product");
            productParams.put("limit", limit);
            productParams.put("sort","");
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
        return jsonRPC2Response;
    }

    private String generateRequestID(){
        return "rid" + new Random().nextInt(Integer.MAX_VALUE);
    }

}
