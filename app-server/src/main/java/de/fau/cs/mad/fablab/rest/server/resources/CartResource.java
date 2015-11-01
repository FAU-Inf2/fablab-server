package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.CartApi;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.cs.mad.fablab.rest.server.core.*;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http403Exception;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;



public class CartResource implements CartApi {

    private final CartFacade facade;

    public CartResource(CartFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public void create(CartServer obj) {
        if(CheckoutResource.getAcceptedCode().equals(obj.getCartCode()))
            this.facade.create(obj);
        else
            throw new Http403Exception("Your cart code (" + obj.getCartCode() + ") is not valid.");
    }

    @UnitOfWork
    @Override
    public CartStatus getStatus(String id) {
        return this.facade.getStatus(id);
    }
}