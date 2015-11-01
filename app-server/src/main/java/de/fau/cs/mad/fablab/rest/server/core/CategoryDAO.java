package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Category;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;


public class CategoryDAO extends AbstractDAO<Category> {


    private static final String QUERY_DELETE_ALL = "delete FROM Category";
    private static final String QUERY_FIND_ALL = "FROM Category";


    public CategoryDAO(SessionFactory aFactory){
        super(aFactory);
    }

    public List<Category> findAll() {
        return super.currentSession().createQuery(QUERY_FIND_ALL).list();
    }


    public Category create(Category aObject){
        return persist(aObject);
    }

    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        super.currentSession().createQuery(QUERY_DELETE_ALL).executeUpdate();
    }


}
