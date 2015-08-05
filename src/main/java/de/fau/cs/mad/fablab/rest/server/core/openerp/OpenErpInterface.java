package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.core.UOM;

import java.util.List;

public interface OpenErpInterface {
    List<UOM> getUOMs() throws OpenErpException;
    List<Location> getLocations();
    List<Category> getCategories() throws OpenErpException;
    List<Product> getProducts(int limit, int offset) throws OpenErpException;
    Product searchForProductsById(String id) throws OpenErpException;
    List<Product> searchForProductsByName(String searchString, int limit, int offset) throws OpenErpException;
    List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException;
}
