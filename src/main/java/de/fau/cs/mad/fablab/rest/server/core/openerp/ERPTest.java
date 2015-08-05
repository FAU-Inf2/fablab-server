package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Location;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.core.UOM;
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
        Product searchedProductId = new Product();
        List<UOM> uoms = new ArrayList<>();


        List<Location> locations = new ArrayList<>();

        try{
            //uoms = openErp.getUOMs();

            products = openErp.getProducts(0,0);
            //searchedProducts = openErp.searchForProductsByName("Holz", 0, 0);
            //searchedProductsCategory = openErp.searchForProductsByCategory("1-reihig", 5, 0);
            //searchedProductId = openErp.searchForProductsById("0662");
            //categories = openErp.getCategories();
            //locations = openErp.getLocations();

        }catch (OpenErpException erp){
            //TODO
        }

        System.out.println("####### Gefundene Objekte: (Alle UOMs)");
        int index = 0;
        for(UOM uom : uoms){
            System.out.println(index + " - " + uom.toString());
            index++;
        }

        System.out.println("####### Gefundene Objekte: (ALle Produkte)" + searchedProducts.size());
        index = 0;
        for(Product pro : products){
            index++;
            System.out.println(index + ": " + pro.toString());
            System.out.println(" - " + pro.getCategory().toString());
            System.out.println(" - " + pro.getLocationObject().toString());
            System.out.println(" - " + pro.getUom().toString());
        }

        System.out.println("####### Gefundene Objekte: (Produktnamen)" + searchedProducts.size());
        index = 0;
        for(Product pro : searchedProducts){
            index++;
            System.out.println(index + ": " + pro.toString());
            System.out.println(" - " + pro.getCategory().toString());
            System.out.println(" - " + pro.getLocationObject().toString());
            System.out.println(" - " + pro.getUom().toString());
        }

        System.out.println("####### Gefundene Objekte (Category): " + searchedProductsCategory.size());
        index = 0;
        for(Product pro : searchedProductsCategory){
            index++;
            System.out.println(index + ": " + pro.toString());
            System.out.println(" - " + pro.getCategory().toString());
            System.out.println(" - " + pro.getLocationObject().toString());
            System.out.println(" - " + pro.getUom().toString());
        }

        System.out.println("####### Gefundenes Objekt (ID): ");
        index = 0;
        System.out.println(index + ": " + searchedProductId.toString());
        System.out.println(" - " + searchedProductId.getCategory().toString());
        System.out.println(" - " + searchedProductId.getLocationObject().toString());
        System.out.println(" - " + searchedProductId.getUom().toString());



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
        }
    }
}
