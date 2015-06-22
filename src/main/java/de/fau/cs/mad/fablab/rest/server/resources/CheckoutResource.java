package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;
import de.fau.cs.mad.fablab.rest.server.core.CartFacade;
import de.fau.cs.mad.fablab.rest.server.core.Checkout;
import io.dropwizard.hibernate.UnitOfWork;

public class CheckoutResource implements Checkout {

    private CartFacade facade;

    public CheckoutResource(CartFacade facade){
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public CartServer getCart(String id) {
        return facade.getCart(id);
    }

    @UnitOfWork
    @Override
    public boolean markCartAsPaid(String id) {
        return this.facade.updateCartStatus(id, CartStatusEnum.PAID);
    }

    @UnitOfWork
    @Override
    public boolean markCartAsCancelled(String id) {
        return this.facade.updateCartStatus(id, CartStatusEnum.CANCELLED);

    }
}
