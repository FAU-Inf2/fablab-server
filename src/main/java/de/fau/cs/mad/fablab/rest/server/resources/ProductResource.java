package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ProductApi;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

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
    public List<Product> findByName(String name, int limit, int offset) {
        return this.facade.findByName(name, limit, offset);
    }

    @UnitOfWork
    @Override
    public List<Product> findByCategory(String category) {
        return this.facade.findByCategory(category);
    }

    @UnitOfWork
    @Override
    public List<Product> findAll(int limit, int offset) {
        return this.facade.findAll(limit, offset);
    }
}