package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
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

        try{
            products = openErp.getProducts(50,0);
            searchedProducts = openErp.searchForProductsByName("Holz", 0, 0);
            searchedProductsCategory = openErp.searchForProductsByCategory("1", 5, 0);
            searchedProductId = openErp.searchForProductsById("0008");
        }catch (OpenErpException erp){
            //TODO
        }

        System.out.println("####### Gefundene Objecte: (ALle Produkte)" + searchedProducts.size());
        int index = 0;
        for(Product pro : products){
            index++;
            System.out.println(index + ": " + pro.getProductId() + " " + pro.getName() + " " + pro.getLocation() + " : "+ pro.getCategoryId());
        }

        System.out.println("####### Gefundene Objecte: (Produktnamen)" + searchedProducts.size());
        index = 0;
        for(Product pro : searchedProducts){
            index++;
            System.out.println(index + " " + pro.getName() + " " + pro.getLocation());
        }

        System.out.println("####### Gefundene Objecte (Category): " + searchedProductsCategory.size());
        index = 0;
        for(Product pro : searchedProductsCategory){
            index++;
            System.out.println(index + " " + pro.getName() + " " + pro.getLocation());
        }

        System.out.println("####### Gefundenes Object (ID)");
        if(!(searchedProductId == null)){
            System.out.println(searchedProductId.getName());
        }
    }
}
