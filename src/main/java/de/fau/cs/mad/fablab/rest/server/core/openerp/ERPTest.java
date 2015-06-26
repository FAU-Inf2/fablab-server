package de.fau.cs.mad.fablab.rest.server.core.openerp;

import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.configuration.OpenErpConfiguration;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class ERPTest {

    public static void main(String[] args){
        OpenErpConfiguration config = new OpenErpConfiguration();
        config.setHostname("https://xn--eichhrnchen-vfb.fablab.fau.de");
        config.setDatabase("appdev");
        config.setPassword("yjaheztoxt");
        config.setUsername("MAD");
        OpenErpInterface openErp = null;
        OpenErpClient.setConfiguration(config);
        openErp = OpenErpClient.getInstance();
        List<Product> products = new ArrayList<>();
        try{
            products = openErp.getProducts(100, 100);
        }catch (OpenErpException erp){
            // TODO
        }
        int index = 0;
        for(Product cat : products) {
            index++;
            System.out.println(index + " - " + cat.getName() + " : " + cat.getLocation());
        }
    }
}
