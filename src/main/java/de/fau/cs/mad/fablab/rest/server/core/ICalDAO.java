package de.fau.cs.mad.fablab.rest.server.core;


import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by EE on 11.05.15.
 */
public class ICalDAO extends AbstractDAO<ICal> {

    public ICalDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public ICal findById(long id) {
        ICal n = super.get(id);
        if(n == null)
            return new ICal();
        return n;
    }

    @SuppressWarnings("unchecked")
    public List<ICal> findAll() {
        return super.currentSession().createQuery("FROM ICal").list();
    }

    //Create
    public ICal create(ICal obj){
        return persist(obj);
    }


    //Update
    public ICal update(ICal modified) {
        ICal stored = this.get(modified.getId());
        stored.setDescription(modified.getDescription());
        stored.setDtend(modified.getDtend());
        stored.setDtstamp(modified.getDtstamp());
        stored.setDtstart(modified.getDtstart());
        stored.setExdate(modified.getExdate());
        stored.setLocation(modified.getLocation());
        stored.setRrule(modified.getRrule());
        stored.setUrl(modified.getUrl());
        this.persist(stored);
        return stored;
    }


    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        currentSession().createQuery("delete FROM ICal").executeUpdate();
    }
}
