package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class DrupalDAO extends AbstractDAO<FabTool> {

    public DrupalDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public FabTool findToolById(long id) {
        return super.get(id);
    }

    @SuppressWarnings("unchecked")
    public List<FabTool> findAllTools() {
        return super.currentSession().createQuery("FROM FabTool").list();
    }

    //Create
    public FabTool create(FabTool obj){
        return persist(obj);
    }


    //Update
    public FabTool update(FabTool modified) {
        return null;
    }


    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        currentSession().createQuery("delete FROM FabTool").executeUpdate();
    }
}
