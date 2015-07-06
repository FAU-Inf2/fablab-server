package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;

public class CartFacade{

    private final CartDAO dao;
    private OldCartRemover remover;

    public CartFacade(CartDAO dao) {
        this.dao = dao;
        this.remover = new OldCartRemover(dao, 30); // Removes old carts after 30 min!
    }

    public CartServer create(CartServer obj) {
        obj.setSentToServer();
        return this.dao.create(obj);
    }

    public CartServer getCart(String id){
        return this.dao.findById(id);
    }

    public CartStatusEnum getStatus(String id) {
        return this.dao.findById(id).getStatus();
    }

    public boolean updateCartStatus(String id, CartStatusEnum status){
        remover.removeAllOldCarts();
        return this.dao.updateCartStatus(id, status);
    }


}
