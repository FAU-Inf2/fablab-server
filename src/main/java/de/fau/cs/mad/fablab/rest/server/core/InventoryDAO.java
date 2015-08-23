
package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.*;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;


public class InventoryDAO extends AbstractDAO<InventoryItem> {

    public InventoryDAO(SessionFactory factory) {
        super(factory);
    }


    @SuppressWarnings("unchecked")
    public List<InventoryItem> findAll() {
        return super.currentSession().createQuery("FROM InventoryItem").list();

    }


    //Create
    public InventoryItem create(InventoryItem obj){
        if(persist(obj) != null)
            return obj;
        return null;
    }

    //Delete
    public boolean deleteAll() {
        currentSession().createQuery("DELETE FROM InventoryItem").executeUpdate();
        return true;
    }

}
