package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpException;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpInterface;

import java.util.ArrayList;
import java.util.List;

public class ProductFacade {

    private final ProductDAO dao;
    private final OpenErpInterface mOpenErp;

    public ProductFacade(ProductDAO dao) {
        this.dao = dao;
        this.mOpenErp = OpenErpClient.getInstance();
    }

    public List<Product> findAll(int limit, int offset) {
        try {
            List<Product> products = mOpenErp.getProducts(limit, offset);
            for(Product tmp : products){
                dao.create(tmp);
            }
            return dao.findAll();
        } catch (OpenErpException e) {
            e.printStackTrace();
            return dao.findAll();
        }
    }

    public List<String> findAllNames() {
        List<Product> products = null;
        try {
            products = mOpenErp.getProducts(Integer.MAX_VALUE, 0);
        } catch (OpenErpException e) {
            products = dao.findAll();
        }
        List<String> names = new ArrayList<>();
        for(Product product : products){
            names.add(product.getName());
        }
        return names;
    }

    public List<Product> findByName(String name, int limit, int offset) {
        try {
            return mOpenErp.searchForProductsByName(name, limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return dao.findByName(name);
        }
    }

    public List<Product> findByCategory(String category, int limit, int offset) {
        try {
            return mOpenErp.searchForProductsByCategory(category, limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return dao.findByCategory(category);
        }
    }

    public Product findById(String id) {
        try {
            return mOpenErp.searchForProductsById(id);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return dao.findById(id);
        }
    }

    public Product create(Product obj) {
        return this.dao.create(obj);
    }

}
