package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.api.CartApi;
import de.fau.cs.mad.fablab.rest.core.Cart;

/**
 * Created by EE on 14.05.15.
 */
public class CartFacade{

    private final CartDAO dao;

    public CartFacade(CartDAO dao) {
        this.dao = dao;
    }

    public Cart create(Cart obj) {
        return this.dao.create(obj);
    }

    public Cart getStatus(String id) {
        return this.dao.findById(id);
    }
}
