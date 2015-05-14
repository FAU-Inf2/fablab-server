package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.CartApi;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */

//TODO: RESPONSES like  404 and so on...
public class CartResource implements CartApi {

    private final CartFacade facade;


    public CartResource(CartFacade facade) {
        this.facade = facade;
    }

    @Override
    public Cart create(Cart obj) {
        return this.facade.create(obj);
    }

    @Override
    public Cart update(Cart obj) {
        return this.facade.update(obj);
    }

    @Override
    public void delete(long id) {
        this.facade.delete(id);
    }
}