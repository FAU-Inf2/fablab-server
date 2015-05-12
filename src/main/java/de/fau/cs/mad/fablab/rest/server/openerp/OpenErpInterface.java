package de.fau.cs.mad.fablab.rest.server.openerp;

import de.fau.cs.mad.fablab.rest.entities.Product;

import java.net.MalformedURLException;
import java.util.List;

public interface OpenErpInterface {

    public void authenticate() throws MalformedURLException;
    public List<Product> getProducts(int limit, int offset);
    public List<Product> searchForProducts(int limit, int offset, String value);

}
