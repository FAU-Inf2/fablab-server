package de.fau.cs.mad.fablab.rest.server.core.openerp;

import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
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
        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);

        try {
            final List<UOM> uoms = uomClient.getUOMs();
            final List<Category> categories = categoryClient.getCategories();
            final List<Location> locations = locationClient.getLocations();
            final List<Product> products = productClient.getProducts(limit, offset);

            prepareCategories(categories, locations);

            for (Product product : products) {
                product = buildProduct(product, categories, uoms);
            }

            return products;
        }catch (Exception e){
            throw new OpenErpException(e.getMessage(), "");
        }
    }

    /***
     * searches for a substring inside product name sat the openerp web service
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
        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);
        try{
            List<UOM> uoms = uomClient.getUOMs();
            List<Category> categories = categoryClient.getCategories();
            List<Location> locations = locationClient.getLocations();
            List<Product> products = productClient.searchForProductsByName(searchString, limit, offset);

            prepareCategories(categories,locations);

            for(Product product : products){
                product = buildProduct(product,categories,uoms);
            }

            return products;
        }catch (Exception e){
            throw new OpenErpException(e.getMessage(), "");
        }
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
        ProductClient productClient = new ProductClient(mOpenERPConnector,mSearchReadUrl);

        try {
            List<UOM> uoms = uomClient.getUOMs();
            List<Category> categories = categoryClient.getCategories();
            List<Location> locations = locationClient.getLocations();
            List<Product> products = productClient.searchForProductsByCategory(searchString, limit, offset);

            prepareCategories(categories, locations);

            for (Product product : products) {
                product = buildProduct(product, categories, uoms);
            }

            return products;
        }catch (Exception e){
            throw new OpenErpException(e.getMessage(), "");
        }
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
        UOMClient uomClient = new UOMClient(mOpenERPConnector,mSearchReadUrl);
        try {
            List<Category> categories = categoryClient.getCategories();
            List<Location> locations = locationClient.getLocations();
            List<UOM> uoms = uomClient.getUOMs();

            prepareCategories(categories, locations);

            product = buildProduct(product, categories, uoms);

            return product;
        }catch (Exception e){
            throw new OpenErpException(e.getMessage(), "");
        }
    }

    /**
     * get all locations
     * @return List of {@link Location}
     */
    public List<Location> getLocations() throws OpenErpException{
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
                category.setLocationString(categ.getLocationString());
                category.setCategories(categ.getCategories());
                category.setLocationObject((categ.getLocationObject()));
                category.setParent_category_id(categ.getParent_category_id());
            }
        }
        return category;
    }

    private void prepareCategories(List<Category> categories, List<Location> locations){
        for(Category category : categories){
            category = buildCategory(category, locations);
        }
    }

    private Category buildCategory(final Category aCategory, final List<Location> aLocations){
        try{
            aCategory.setLocationObject(searchLocationByCategory(aCategory,aLocations));
        }catch (LocationNotFoundException e) {
            e.printStackTrace();
        }
        return aCategory;
    }


    private Product buildProduct(Product aProduct, List<Category> aCategories,List<UOM> aUOMs){
        aProduct.setCategory(getCategoryObjectById(aCategories, aProduct.getCategoryId()));
        aProduct.setUom(getUOMById(aUOMs, aProduct.getOum_id()));
        aProduct = prepareProductLocation(aProduct,aCategories);
        if(aProduct.getLocation().contains(OpenERPConst.UNKNOW_LOCATION)){
           aProduct.setLocationForProductMap(OpenERPConst.UNKNOW_LOCATION);
        }
        else{
            aProduct.setLocationForProductMap(prepareLocationForProductMap(aProduct.getLocation()));
        }
        return aProduct;
    }

    private String prepareLocationForProductMap(String location) {
        location = location.replace("  / ", "/");
        location = location.replace(" / ","/");
        location = location.replace(" ","_");
        location = location.replace("ä", "ae");
        location = location.replace("ö", "oe");
        location = location.replace("ü", "ue");
        location = location.replace("Ä", "Ae");
        location = location.replace("Ö", "Oe");
        location = location.replace("Ü", "Ue");
        location = location.replace("ß", "ss");

        return location;
    }

    private Product prepareProductLocation(Product aProduct,List<Category> aCategories){
        if(aProduct.getLocation().equals(OpenERPConst.UNKNOW_LOCATION)){
            String categoryLocationString = aProduct.getCategory().getLocationString();
            aProduct.setLocation(categoryLocationString);
        }
        return aProduct;
    }

    private Location searchLocationByCategory(Category aCategory, List<Location> aLocations) throws LocationNotFoundException{
        if(aCategory.getLocation_id() != 0) {
            for (Location location : aLocations) {
                if (aCategory.getLocation_id() == location.getLocationId()) {
                    return location;
                }
            }
            throw new LocationNotFoundException("Location was not in the list -  locationId: " + aCategory.getLocation_id());
        }
        return new Location();
    }
}
