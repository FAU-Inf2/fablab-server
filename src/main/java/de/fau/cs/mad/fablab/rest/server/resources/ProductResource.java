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


    @Override
    public Product findById(@PathParam("id") long id) {
        return this.facade.findById(id);
    }

    @Override
    public List<Product> findByName(@PathParam("name") String name) {
        return this.facade.findByName(name);
    }

    @Override
    public List<Product> findByCategory(@PathParam("category") String category) {
        return this.facade.findByCategory(category);
    }

    @Override
    public List<Product> findAll() {
        return this.facade.findAll();
    }
}