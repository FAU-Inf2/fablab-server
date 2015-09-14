package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpInterface;

import java.util.ArrayList;
import java.util.List;

public class ProductFacade {

    private final ProductDAO dao;

    public ProductFacade(ProductDAO dao) {
        this.dao = dao;

    }

    public List<Product> findAll(int limit, int offset) {
        return dao.findAll();
    }

    public List<String> findAllNames() {
        List<Product> products = dao.findAll();
        List<String> names = new ArrayList<>();
        for(Product product : products){
            names.add(product.getName());
        }
        return names;
    }

    public List<Product> findAllWithoutFilter(int limit, int offset){
        return dao.findAllWithoutFilter();
    }

    public List<Product> findByName(String name, int limit, int offset) {
        return dao.findByName(name);
    }

    public List<Product> findByCategory(String category, int limit, int offset) {
        return dao.findByCategory(category);
    }

    public Product findById(String id) {
        return dao.findById(id);
    }

    public Product create(Product obj) {
        return this.dao.create(obj);
    }

}
