package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.drupal.ICalClient;
import de.fau.cs.mad.fablab.rest.server.drupal.ICalInterface;

import java.util.List;

/**
 * Created by EE on 14.05.15.
 */
public class ICalFacade {

    private final ICalDAO dao;
    private final ICalInterface iCalInterface;

    public ICalFacade(ICalDAO dao) {
        this.dao = dao;
        this.iCalInterface = ICalClient.getInstance();
        //insertElements();
    }

    public ICal findById(Long id) {
        //return this.dao.findById(id);
        return iCalInterface.findById(id);
    }

    public List<ICal> findAll() {
        //return this.dao.findAll();
        return iCalInterface.findAll();
    }

    public void updateAll() {
        List<ICal> events = iCalInterface.findAll();
        for (ICal event : events) {
            dao.update(event);
        }
    }

    private void insertElements() {
        List<ICal> events = iCalInterface.findAll();
        for (ICal event : events) {
            dao.create(event);
        }
    }

}
