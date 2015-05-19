package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Product;

import java.util.List;

public class ProductFacade {
    private final ProductDAO dao;

    public ProductFacade(ProductDAO dao) {
        this.dao = dao;
    }

    public Product findById(Long id) {
        return this.dao.findById(id);
    }

    public List<Product> findAll(int limit, int offset) {
        return this.dao.findAll();
    }

    public List<Product> findByName(String name, int limit, int offset) {
        return this.dao.findByName(name);
    }

    public List<Product> findByCategory(String category) {
        return this.dao.findByCategory(category);
    }

    public Product create(Product obj) {
        return this.dao.create(obj);
    }

}
