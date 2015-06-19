package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.*;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class CartDAO extends AbstractDAO<CartServer> {

    public CartDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public CartServer findById(String id) {
        System.out.println("SEARCHING CART FOR CODE: " + id);
        List<CartServer> cartEntries = super.currentSession().createQuery("FROM CartServer").list();

        for(CartServer cart : cartEntries) {
            System.out.println("GOT CART: " + cart.getCartCode());
            for(CartEntryServer cartEntry : cart.getItems())
                System.out.println("WITH PRODUCT: " + cartEntry.getId() + " WITH AMOUNT" + cartEntry.getAmount());
        }
        return super.get(id);
    }

    //Create
    public CartServer create(CartServer obj){
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
