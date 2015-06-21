package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;

public class CartFacade{

    private final CartDAO dao;

    public CartFacade(CartDAO dao) {
        this.dao = dao;
    }

    public CartServer create(CartServer obj) {
        return this.dao.create(obj);
    }

    public CartServer getCart(String id){
        return this.dao.findById(id);
    }

    public CartStatusEnum getStatus(String id) {
        return this.dao.findById(id).getStatus();
    }

    public void updateCartStatus(String id, CartStatusEnum status){
        this.dao.updateCartStatus(id, status);
    }
}
