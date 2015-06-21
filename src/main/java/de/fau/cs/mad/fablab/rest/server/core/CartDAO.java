package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.*;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;


public class CartDAO extends AbstractDAO<CartServer> {

    public CartDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public CartServer findById(String id) {
        CartServer cartServer = super.get(id);
        return cartServer;
    }

    //Create
    public CartServer create(CartServer obj){
        for(CartEntryServer item : obj.getItems())
            item.setCart(obj);
        return persist(obj);
    }

    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;
        currentSession().delete(get(id));
        return true;
    }

    public void updateCartStatus(String id, CartStatusEnum status) {
        CartServer cart = super.get(id);
        //ignore if there is no such cart
        if (cart == null)
            return;

        cart.setStatus(status);
        super.persist(cart);
    }
}
