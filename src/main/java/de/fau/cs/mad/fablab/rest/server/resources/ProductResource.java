package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ProductApi;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.InternalServerErrorException;
import java.util.List;

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
        List<Product> result = this.facade.findByName(name, limit, offset);
        if(result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<Product> findByCategory(String category, int limit, int offset) {
        return this.facade.findByCategory(category, limit, offset);
    }

    @UnitOfWork
    @Override
    public List<Product> findAll(int limit, int offset) {

        List<Product> result = this.facade.findAll(limit, offset);
        if(result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }

    @Override
    public List<String> findAllNames() {
        List<String> result = this.facade.findAllNames();
        if(result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }
}