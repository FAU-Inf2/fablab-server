package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ProductApi;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.List;

public class ProductResource implements ProductApi {

    private final ProductFacade facade;


    public ProductResource(ProductFacade facade) {
        this.facade = facade;
    }


    @UnitOfWork
    @Override
    public Product findById(String id) {
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

    @UnitOfWork
    @Override
    public List<Product> findAllWithoutFilters(int limit, int offset) {
        List<Product> result = this.facade.findAllWithoutFilter(limit, offset);
        if(result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<String> findAllNames() {
        List<String> result = this.facade.findAllNames();
        if(result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<String> getAutoCompletions() {
        List<String> strings = this.facade.findAllNames();
        List<String> tempList = new ArrayList<>();
        if(strings != null) {
            for (int index = 0; index < strings.size(); index++) {
                String[] temp = strings.get(index).replace(",", " ").replace("(", " ").replace(")", " ")
                        .replace("_", " ").replace("-", " ").split(" ");
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].length() > 2) {
                        boolean found = false;
                        for (String stringValue : tempList)
                            if (stringValue.toLowerCase().equals(temp[j].toLowerCase()))
                                found = true;
                        if (!found)
                            tempList.add(temp[j]);
                    }
                }
            }
            if (strings == null) {
                throw new InternalServerErrorException("There is a problem getting the results");
            }
        }
        return tempList;
    }

}