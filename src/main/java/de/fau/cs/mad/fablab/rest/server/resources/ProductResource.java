package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.api.ProductApi;
import de.fau.cs.mad.fablab.rest.entities.Product;

import java.util.ArrayList;
import java.util.List;

/***
 * Resource class for our /products uri
 */
public class ProductResource implements ProductApi
{
    private static List<Product> dummyProducts;

    public ProductResource() {
        dummyProducts = new ArrayList<Product>();

        dummyProducts.add(new Product(1253, "Infrarotdiode 950nm", 60, "reichelt.de: LD 271"));
        dummyProducts.add(new Product(309, "LED 3mm gelb transparemt gelb, durchsichtig (3mm bedrahtet)", 5, "Pollin"));
        dummyProducts.add(new Product(310, "LED 3mm grün grün(3mm bedrahtet)", 5, "Pollin"));
        dummyProducts.add(new Product(308, "LED 3mm rot rot(3mm bedrahtet)", 5,"Pollin:"));
        dummyProducts.add(new Product(307, "LED 3mm rot transparent rot, durchsichtig(3mm bedrahtet)", 5, "Pollin:"));
        dummyProducts.add(new Product(312, "LED 4,2mm grün grün (4,2mm bedrahtet)", 2, "Pollin:"));
        dummyProducts.add(new Product(311, "LED 5mm blau blau (5mm bedrahtet)", 5, "Pollin:"));
        dummyProducts.add(new Product(315, "LED 5mm gelb gelb (5mm bedrahtet)", 5, "Pollin:"));
        dummyProducts.add(new Product(314, "LED 5mm grün grün (5mm bedrahtet)", 5, "Pollin:"));
        dummyProducts.add(new Product(429, "LED 5mm rot diffus 90° bedrahtet", 7, "Michael Jäger: 5R-DR-500"));
        dummyProducts.add(new Product(316, "LED 5mm rot rot(5mm bedrahtet)", 8, "Pollin:"));
    }

    @Override
    public List<Product> getProducts() {
        return dummyProducts;
    }

    @Override
    public Product getProduct(int id) {
        for (Product p : dummyProducts)
        {
            if (p.getId() == id)
                return p;
        }

        //todo: we have to set status code to 404
        return null;
    }
}
