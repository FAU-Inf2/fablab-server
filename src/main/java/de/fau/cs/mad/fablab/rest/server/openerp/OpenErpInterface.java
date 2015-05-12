package de.fau.cs.mad.fablab.rest.server.openerp;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import java.net.MalformedURLException;

public interface OpenErpInterface {

    public void authenticate() throws MalformedURLException;
    public JSONRPC2Response getProducts(int limit, int offset);
    public JSONRPC2Response searchForProducts(int limit, int offset, String value);

}
