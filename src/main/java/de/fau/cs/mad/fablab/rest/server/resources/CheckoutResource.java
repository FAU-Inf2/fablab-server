package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;
import de.fau.cs.mad.fablab.rest.server.core.CartFacade;

public class CheckoutResource implements Checkout {

    private CartFacade facade;

    public CheckoutResource(CartFacade facade){
        this.facade = facade;
    }

    @Override
    public Cart getCart(String id) {
        return facade.getCart(id);
    }

    @Override
    public void markCartAsPaid(String id) {
        this.facade.updateCartStatus(id, CartStatusEnum.PAID);
    }

    @Override
    public void markCartAsCancelled(String id) {
        this.facade.updateCartStatus(id, CartStatusEnum.CANCELLED);

    }
}
