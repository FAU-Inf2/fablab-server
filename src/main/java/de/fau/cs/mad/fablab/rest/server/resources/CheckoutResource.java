package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatusEnum;
import de.fau.cs.mad.fablab.rest.server.core.CartFacade;
import de.fau.cs.mad.fablab.rest.server.core.Checkout;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http401Exception;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.Random;

public class CheckoutResource implements Checkout {

    private CartFacade facade;
    private static long acceptedCode;

    public CheckoutResource(CartFacade facade){
        this.facade = facade;
    }

    @Override
    public long createCode(String password){
        System.out.println("PASSWORD: " + password);
        //TODO
        // Set password on serverstart
        // -> as long as we use the checkoutSimulator password will always be public
        // -> atm it makes no sense at all.
        if(password.equals("dummyPassword"))
            return acceptedCode = (new Random()).nextInt();
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
        return this.facade.updateCartStatus(id, CartStatusEnum.PAID);
    }

    @UnitOfWork
    @Override
    public boolean markCartAsCancelled(String id) {
        return this.facade.updateCartStatus(id, CartStatusEnum.CANCELLED);

    }

    public static long getAcceptedCode() {
        return acceptedCode;
    }
}
