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
            return mOpenErp.getProducts(limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();
        try {
            for(Product p : mOpenErp.getProducts(Integer.MAX_VALUE, 0))
                names.add(p.getName());
        } catch (OpenErpException e) {
            e.printStackTrace();
        }
        return names;
    }

    public List<Product> findByName(String name, int limit, int offset) {
        try {
            return mOpenErp.searchForProductsByName(name, limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> findByCategory(String category, int limit, int offset) {
        try {
            return mOpenErp.searchForProductsByCategory(category, limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product findById(String id) {
        try {
            return mOpenErp.searchForProductsById(id);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Product create(Product obj) {
        return this.dao.create(obj);
    }

}
