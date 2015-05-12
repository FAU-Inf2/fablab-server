package de.fau.cs.mad.fablab.rest.server.core;


import java.util.List;

/**
 * Created by EE on 12.05.15.
 */


public interface ICalApi {

    // Get
    ICal findById(long id);
    List<ICal> findAll();

    // Create
    ICal create(ICal ical);

    // Update
    ICal update(ICal obj);


    // Delete
    void delete(long id);
    void deleteAll();

}
