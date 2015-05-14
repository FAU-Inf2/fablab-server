package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.api.ProductApi;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */
//TODO: RESPONSES like  404 and so on...
public class ProductResource implements ProductApi {

    private final ProductFacade facade;


    public ProductResource(ProductFacade facade) {
        this.facade = facade;
    }


    @UnitOfWork
    @Override
    public Product findById(long id) {
        return this.facade.findById(id);
    }

    @UnitOfWork
    @Override
    public List<Product> findByName(String name) {
        return this.facade.findByName(name);
    }

    @UnitOfWork
    @Override
    public List<Product> findByCategory(String category) {
        return this.facade.findByCategory(category);
    }

    @UnitOfWork
    @Override
    public List<Product> findAll() {
        return this.facade.findAll();
    }
}