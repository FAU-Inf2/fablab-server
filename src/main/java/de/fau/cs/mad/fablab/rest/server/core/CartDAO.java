package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Cart;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class CartDAO extends AbstractDAO<Cart> {

    public CartDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public Cart findById(String id) {
        return super.get(id);
    }

    //Create
    public Cart create(Cart obj){
        return persist(obj);
    }

    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;
        currentSession().delete(get(id));
        return true;
    }

}
