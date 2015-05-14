package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Product;

import java.util.List;

/**
 * Created by EE on 14.05.15.
 */
public class ProductFacade {
    private final ProductDAO dao;

    public ProductFacade(ProductDAO dao) {
        this.dao = dao;
    }

    public Product findById(Long id) {
        return this.dao.findById(id);
    }

    public List<Product> findAll(){
        return this.dao.findAll();
    }

    public List<Product> findByName(String name) {
        return this.dao.findByName(name);
    }

    public  List<Product> findByCategory(String cat){
        return this.dao.findByCategory(cat);
    }

    public Product create(Product obj) {
        return this.dao.create(obj);
    }

}
