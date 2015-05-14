package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;

import java.util.List;

/**
 * Created by EE on 14.05.15.
 */
public class ICalFacade {

    private final ICalDAO dao;

    public ICalFacade(ICalDAO dao) {
        this.dao = dao;
    }

    public ICal findById(Long id) {
        return this.dao.findById(id);
    }

    public List<ICal> findAll(){
        return this.dao.findAll();
    }


}
