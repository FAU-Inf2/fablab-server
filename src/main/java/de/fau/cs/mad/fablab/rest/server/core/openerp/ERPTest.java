package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.configuration.OpenErpConfiguration;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class ERPTest {

    public static void main(String[] args) {
        OpenErpConfiguration config = new OpenErpConfiguration();
        config.setHostname("");
        config.setDatabase("");
        config.setPassword("");
        config.setUsername("");
        OpenErpInterface openErp = null;
        OpenErpClient.setConfiguration(config);
        openErp = OpenErpClient.getInstance();
        List<Product> products = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Product> searchedProducts = new ArrayList<>();
        List<Product> searchedProductsCategory = new ArrayList<>();
        Product searchedProductId = null;


        List<Location> locations = new ArrayList<>();

        try{
            //products = openErp.getProducts(0,0);
            //searchedProducts = openErp.searchForProductsByName("Holz", 0, 0);
            //searchedProductsCategory = openErp.searchForProductsByCategory("1", 5, 0);
            //searchedProductId = openErp.searchForProductsById("0008");
            categories = openErp.getCategories();
            //locations = openErp.getLocations();

        }catch (OpenErpException erp){
            //TODO
        }

        System.out.println("####### Gefundene Objekte: (ALle Produkte)" + searchedProducts.size());
        int index = 0;
        for(Product pro : products){
            index++;
            System.out.println(index + ": " + pro.getProductId() + " " + pro.getName() + " : " + pro.getLocationObject().getName() + " : "+ pro.getCategory().getName());
        }

        System.out.println("####### Gefundene Objekte: (Produktnamen)" + searchedProducts.size());
        index = 0;
        for(Product pro : searchedProducts){
            index++;
            System.out.println(index + " " + pro.getName() + " " + pro.getLocation());
        }

        System.out.println("####### Gefundene Objekte (Category): " + searchedProductsCategory.size());
        index = 0;
        for(Product pro : searchedProductsCategory){
            index++;
            System.out.println(index + " " + pro.getName() + " " + pro.getLocation());
        }

        System.out.println("####### Gefundenes Objekt (ID)");
        if(!(searchedProductId == null)){
            System.out.println(searchedProductId.getName()  + " " + searchedProductId.getLocation_id());
        }

        System.out.println("####### Gefundene Objekte (All Locations): " + searchedProductsCategory.size());
        index = 0;
        for(Location loc : locations){
            index++;
            System.out.println(index + " " + loc.getId() + " " + loc.getName() + " " + loc.getCode());
        }

        System.out.println("####### Gefundene Objekte (Alle Kategorien): " + searchedProductsCategory.size());
        index = 0;
        for(Category loc : categories){
            index++;
            System.out.println(index + " " + loc.getName() + " " + loc.getLocation_id());

            for(Long aChildId : loc.getCategories()){
                System.out.print(aChildId + " ");
            }
            if(loc.getCategories().size() > 0){
                System.out.println();
            }
        }
    }
}
