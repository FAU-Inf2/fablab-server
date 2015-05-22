package de.fau.cs.mad.fablab.rest.server.openerp;

import de.fau.cs.mad.fablab.rest.core.Product;

import java.net.MalformedURLException;
import java.util.List;

public interface OpenErpInterface {
    List<Product> getProducts(int limit, int offset) throws OpenErpException;
    Product searchForProductsById(long id) throws OpenErpException;
    List<Product> searchForProductsByName(String searchString, int limit, int offset) throws OpenErpException;
    List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException;
}
