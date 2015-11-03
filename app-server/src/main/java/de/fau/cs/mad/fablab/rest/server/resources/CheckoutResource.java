package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.server.core.CartFacade;
import de.fau.cs.mad.fablab.rest.server.core.Checkout;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http401Exception;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.Random;

public class CheckoutResource implements Checkout {

    private CartFacade facade;
    private static String acceptedCode;
    private final String apiKey;

    public CheckoutResource(CartFacade facade, String apiKey){
        this.facade = facade;
        this.apiKey = apiKey;
    }

    @Override
    public String createCode(String password){
        if(password.equals(apiKey))
            return acceptedCode = "FAU" + (new Random()).nextInt();
        if(password.length() == 0)
            throw new Http401Exception("No password given!");
        else
            throw new Http401Exception("Wrong password");
    }

    @UnitOfWork
    @Override
    public CartServer getCart(String id) {
        return facade.getCart(id);
    }

    @UnitOfWork
    @Override
    public boolean markCartAsPaid(String id) {
        return this.facade.updateCartStatus(id, CartStatus.PAID);
    }

    @UnitOfWork
    @Override
    public boolean markCartAsCancelled(String id) {
        return this.facade.updateCartStatus(id, CartStatus.CANCELLED);

    }

    public static String getAcceptedCode() {
        return acceptedCode;
    }
}
