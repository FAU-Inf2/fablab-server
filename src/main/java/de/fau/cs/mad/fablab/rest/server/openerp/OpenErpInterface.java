package de.fau.cs.mad.fablab.rest.server.openerp;

import de.fau.cs.mad.fablab.rest.core.Product;

import java.net.MalformedURLException;
import java.util.List;

public interface OpenErpInterface {
    List<Product> getProducts(int limit, int offset) throws OpenErpException;
    List<Product> searchForProducts(String searchString, int limit, int offset) throws OpenErpException;
}
