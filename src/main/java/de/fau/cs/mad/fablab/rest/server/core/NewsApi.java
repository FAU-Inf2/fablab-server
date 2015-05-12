package de.fau.cs.mad.fablab.rest.server.core;


import java.util.List;

/**
 * Created by EE on 12.05.15.
 */


public interface NewsApi {

    // Get
    News findById(long id);
    List<News> findAll();

    // Create
    News create(News news);

    // Update
    News update(News news);


    // Delete
    void delete(long id);
    void deleteAll();

}
