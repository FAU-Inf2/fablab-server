package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import de.fau.cs.mad.fablab.rest.core.Product;

import java.util.List;

public interface OpenErpInterface {

    List<Location> getLocations();
    Location getLocationById(final List<Location> aLocations,long aLocationId);

    List<Category> getCategories() throws OpenErpException;

    List<Product> getProducts(int limit, int offset) throws OpenErpException;
    Product searchForProductsById(String id) throws OpenErpException;
    List<Product> searchForProductsByName(String searchString, int limit, int offset) throws OpenErpException;
    List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException;
}
