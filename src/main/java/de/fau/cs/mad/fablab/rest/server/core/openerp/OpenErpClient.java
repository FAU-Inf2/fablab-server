package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.core.UOM;
import de.fau.cs.mad.fablab.rest.server.configuration.OpenErpConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class OpenErpClient implements OpenErpInterface {

    private static OpenErpInterface instance;
    private static OpenErpConfiguration config = null;

    private URL mSearchReadUrl;
    private OpenERPConnector mOpenERPConnector;

    /***
     * Singleton getInstance()
     *
     * @return
     */
    public static OpenErpInterface getInstance() {
        if (instance == null) {
            try {
                instance = new OpenErpClient();
            } catch (MalformedURLException e) {
                System.err.println("ERROR - MalformedURLException while initializing OpenErpClient. \n" +
                                           "The Reason is : " + e.getMessage() + "\n" +
                                           "Your hostname is : " + config.getHostname());
                System.exit(1);
            }
        }
        return instance;
    }

    public static void setConfiguration(OpenErpConfiguration c) {
        config = c;
    }

    /***
     * Creates a jsonSession and gets a session id by calling authenticate()
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     *
     * @throws MalformedURLException if the hostname is not a valid url
     */
    private OpenErpClient() throws MalformedURLException {
        //If any of these variables is null, print an error message and exit(1)
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing OpenErpClient. Configuration vars missing.\n" +
                                       "The configuration (username, password, hostname and database) has to be set \n " +
                                       "using the class OpenErpConfiguration.\n");
            System.exit(1);
        }
        mSearchReadUrl = new URL(config.getHostname() + OpenERPConst.REQUEST_SEARCH_READ);
        mOpenERPConnector = new OpenERPConnector(config,OpenERPConst.REQUEST_AUTHENTICATE);
        mOpenERPConnector.authenticate();
    }

    /***
     * Requests a lists of all Categories as a tree
     *
     * @return List of {@link Category}
     * @throws OpenErpException
     */
    @Override
    public List<Category> getCategories() throws OpenErpException{
        CategoryClient categoryClient = new CategoryClient(mOpenERPConnector,mSearchReadUrl);
        return categoryClient.getCategories();
    }

    /***
     * Requests a list of products at the openerp web service
     *
     * @param limit  the maximum number of products to return
     * @param offset the record offset
     * @return List of {@link Product} with Object {@link Category}, {@link Location} and {@link UOM} for each Product
     */
    @Override
    public List<Product> getProducts(int limit, int offset) throws OpenErpException {
        CategoryClient categoryClient = new CategoryClient(mOpenERPConnector,mSearchReadUrl);
        LocationClient locationClient = new LocationClient(mOpenERPConnector,mSearchReadUrl);
        UOMClient uomClient = new UOMClient(mOpenERPConnector,mSearchReadUrl);
        List<UOM> uoms = uomClient.getUOMs();
        List<Category> categories = categoryClient.getCategories();
        List<Location> locations = locationClient.getLocations();

        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);
        List<Product> products = productClient.getProducts(limit,offset);

        for(Product product : products){
            product = buildProduct(product,categories,locations, uoms);
        }

        return products;
    }

    /***
     * searches for a substring inside product namesat the openerp web service
     *
     * @param searchString the substring to search for inside product names
     * @param limit        the maximum number of products to return
     * @param offset       the record offset
     * @return List of {@link Product} with Object {@link Category}, {@link Location} and {@link UOM} for each Product
     */
    @Override
    public List<Product> searchForProductsByName(String searchString, int limit, int offset) throws OpenErpException {
        CategoryClient categoryClient = new CategoryClient(mOpenERPConnector,mSearchReadUrl);
        LocationClient locationClient = new LocationClient(mOpenERPConnector,mSearchReadUrl);
        UOMClient uomClient = new UOMClient(mOpenERPConnector,mSearchReadUrl);
        List<UOM> uoms = uomClient.getUOMs();
        List<Category> categories = categoryClient.getCategories();
        List<Location> locations = locationClient.getLocations();

        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);
        List<Product> products = productClient.searchForProductsByName(searchString, limit, offset);

        for(Product product : products){
            product = buildProduct(product,categories,locations, uoms);
        }

        return products;
    }

    /**
     *
     * @param searchString the substring to search for products of a category name
     * @param limit        the maximum number of products to return
     * @param offset       the record offset
     * @return List of {@link Product} with Object {@link Category}, {@link Location} and {@link UOM} for each Product
     * @throws OpenErpException
     */
    @Override
    public List<Product> searchForProductsByCategory(String searchString, int limit, int offset) throws OpenErpException {
        CategoryClient categoryClient = new CategoryClient(mOpenERPConnector,mSearchReadUrl);
        LocationClient locationClient = new LocationClient(mOpenERPConnector,mSearchReadUrl);
        UOMClient uomClient = new UOMClient(mOpenERPConnector,mSearchReadUrl);
        List<UOM> uoms = uomClient.getUOMs();
        List<Category> categories = categoryClient.getCategories();
        List<Location> locations = locationClient.getLocations();

        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);
        List<Product> products = productClient.searchForProductsByCategory(searchString, limit, offset);

        for(Product product : products){
            product = buildProduct(product,categories,locations, uoms);
        }

        return products;
    }

    /***
     * @param id a string containing 4 digits
     * @return
     * @throws OpenErpException
     */
    @Override
    public Product searchForProductsById(String id) throws OpenErpException {
        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);
        Product product = productClient.searchForProductsById(id);

        if(product == null)
            return null;

        CategoryClient categoryClient = new CategoryClient(mOpenERPConnector,mSearchReadUrl);
        LocationClient locationClient = new LocationClient(mOpenERPConnector,mSearchReadUrl);

        List<Category> categories = categoryClient.getCategories();
        List<Location> locations = locationClient.getLocations();
        UOMClient uomClient = new UOMClient(mOpenERPConnector,mSearchReadUrl);
        List<UOM> uoms = uomClient.getUOMs();
        product = buildProduct(product,categories,locations, uoms);

        return product;
    }

    /**
     * get all locations
     * @return List of {@link Location}
     */
    public List<Location> getLocations(){
        LocationClient locationClient = new LocationClient(mOpenERPConnector,mSearchReadUrl);
        return locationClient.getLocations();
    }

    /**
     * get all uom objects
     * @return List of {@link UOM}
     * @throws OpenErpException
     */
    public List<UOM> getUOMs() throws OpenErpException{
        UOMClient uomClient = new UOMClient(mOpenERPConnector,mSearchReadUrl);
       return uomClient.getUOMs();
    }



    private Product buildProduct(Product aProduct, List<Category> aCategory, List<Location> aLocations, List<UOM> aUOMs){
        aProduct.setCategory(getCategoryObjectById(aCategory, aProduct.getCategoryId()));
        aProduct.setLocationObject(getLocationById(aLocations, aProduct.getLocation_id()));
        aProduct.setUom(getUOMById(aUOMs, aProduct.getOum_id()));
        return aProduct;
    }

    private UOM getUOMById(List<UOM> aUOMs,long aUom_id){
        UOM newOUM = new UOM();
        for(UOM uom : aUOMs){
            if(aUom_id == uom.getUom_id()){
                newOUM.setUom_id(uom.getUom_id());
                newOUM.setName(uom.getName());
                newOUM.setRounding(uom.getRounding());
                newOUM.setUomType(uom.getUomType());
                return newOUM;

            }
        }
        System.out.println("Found no OUM");
        return newOUM;
    }

    private Location getLocationById(final List<Location> aLocations,final long aLocationId){
        Location newLocation = new Location();
        for(Location location : aLocations){
            if(location.getId() == aLocationId){
                newLocation.setId(location.getId());
                newLocation.setName(location.getName());
                newLocation.setCode(location.getCode());
                return newLocation;
            }
        }
        return newLocation;
    }

    private Category getCategoryObjectById(List<Category> categories, long id) {
        Category category = new Category();
        for (Category categ : categories) {
            if (categ.getCategoryId() == id) {
                category.setCategoryId(categ.getCategoryId());
                category.setName(categ.getName());
                category.setLocation_id(categ.getLocation_id());
                category.setCategories(categ.getCategories());
            }
        }
        return category;
    }
}
