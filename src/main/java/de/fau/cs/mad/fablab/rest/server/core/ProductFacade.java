package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpException;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpInterface;

import java.util.List;

public class ProductFacade {

    private final ProductDAO dao;
    private final OpenErpInterface mOpenErp;

    public ProductFacade(ProductDAO dao) {
        this.dao = dao;
        this.mOpenErp = OpenErpClient.getInstance();
    }

    public Product findById(Long id) {
        return this.dao.findById(id);
    }

    public List<Product> findAll(int limit, int offset) {
        try {
            return mOpenErp.getProducts(limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> findByName(String name, int limit, int offset) {
        try {
            return mOpenErp.searchForProducts(name, limit, offset);
        } catch (OpenErpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> findByCategory(String category) {
        return this.dao.findByCategory(category);
    }

    public Product create(Product obj) {
        return this.dao.create(obj);
    }

}
