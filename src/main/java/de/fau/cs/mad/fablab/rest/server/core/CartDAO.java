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
    public CartServer findById(String code) {
        CartServer cartServer = super.get(code);
        return cartServer;
    }

    @SuppressWarnings("unchecked")
    public List<CartServer> findAll() {
        return super.currentSession().createQuery("FROM CartServer").list();
    }


    //Create
    public CartServer create(CartServer obj){
        for(CartEntryServer item : obj.getItems())
            item.setCart(obj);
        return persist(obj);
    }

    //Delete
    public boolean delete(String code) {
        if (get(code) == null)
            return false;
        currentSession().delete(get(code));
        return true;
    }

    public boolean updateCartStatus(String code, CartStatusEnum status) {
        CartServer cart = super.get(code);
        //ignore if there is no such cart
        if (cart == null)
            return false;

        //Just to get sure, that status cannot be changed again after paid/cancelled
        if(cart.getStatus() == CartStatusEnum.PENDING) {
            cart.setStatus(status);
            super.persist(cart);
            return true;
        }
        return false;
    }

}
