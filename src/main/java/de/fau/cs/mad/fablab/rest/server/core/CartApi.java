package de.fau.cs.mad.fablab.rest.server.core;


import java.util.List;

/**
 * Created by EE on 12.05.15.
 */


public interface CartApi {

    // Get
    Cart findById(long id);
    List<Cart> findAll();

    // Create
    Cart create(Cart obj);

    // Update
    Cart update(Cart obj);


    // Delete
    void delete(long id);

}
