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
        List<Cart> cartEntries = super.currentSession().createQuery("FROM CartServer").list();

        for(Cart cart : cartEntries) {
            System.out.println("GOT CART: " + cart.getCartCode());
            for(CartEntry cartEntry : cart.getProducts())
                System.out.println("WITH PRODUCT: " + cartEntry.getProduct().getName());
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
