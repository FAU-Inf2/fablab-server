package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;
import de.fau.cs.mad.fablab.rest.core.Product;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class CartDAO extends AbstractDAO<Cart> {

    public CartDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public Cart findById(String id) {
        System.out.println("SEARCHING CART FOR CODE: " + id);
        List<Cart> cartEntries = super.currentSession().createQuery("FROM Cart").list();

        for(Cart cart : cartEntries) {
            System.out.println("GOT CART: " + cart.getCartCode());
            for(CartEntry cartEntry : cart.getProducts())
                System.out.println("WITH PRODUCT: " + cartEntry.getProduct().getName());
        }
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

    public void updateCartStatus(String id, CartStatusEnum status) {
        Cart cart = super.get(id);
        //ignore if there is no such cart
        if (cart == null)
            return;

        cart.setStatus(status);
        super.persist(cart);
    }
}
