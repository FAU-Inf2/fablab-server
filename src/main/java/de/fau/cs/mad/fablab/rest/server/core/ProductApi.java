package de.fau.cs.mad.fablab.rest.server.core;


import java.util.List;

/**
 * Created by EE on 12.05.15.
 */


public interface ProductApi {

    // Get
    Product findById(long id);
    List<Product> findAll();

    // Create
    Product create(Product obj);

    // Update
    Product update(Product obj);


    // Delete
    void delete(long id);
    void deleteAll();

}
