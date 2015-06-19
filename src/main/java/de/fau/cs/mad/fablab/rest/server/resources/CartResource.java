package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.CartApi;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */

public class CartResource implements CartApi {

    private final CartFacade facade;

    public CartResource(CartFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public void create(CartServer obj) {
         this.facade.create(obj);
    }

    @UnitOfWork
    @Override
    public CartStatusEnum getStatus(String id) {
        return this.facade.getStatus(id);
    }
}