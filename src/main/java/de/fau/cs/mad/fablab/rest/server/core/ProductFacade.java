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
    private boolean initialized = false;

    public ProductFacade(ProductDAO dao) {
        this.dao = dao;
        this.mOpenErp = OpenErpClient.getInstance();
    }

    /***
     * Use this method to create or update the dao store
     */
    public void initializeDao(){
        //TODO
        if(initialized) return;

        List<Product> products = null;
        try {
            products = mOpenErp.getProducts(0, 0);
            for(Product tmp : products){
                dao.create(tmp);
            }
            initialized = true;
        } catch (OpenErpException e) {
            e.printStackTrace();
        }
    }

    public List<Product> findAll(int limit, int offset) {
        initializeDao();
        return dao.findAll();
    }

    public List<String> findAllNames() {
        initializeDao();
        List<Product> products = dao.findAll();
        List<String> names = new ArrayList<>();
        for(Product product : products){
            names.add(product.getName());
        }
        return names;
    }

    public List<Product> findByName(String name, int limit, int offset) {
        initializeDao();
        return dao.findByName(name);
    }

    public List<Product> findByCategory(String category, int limit, int offset) {
        initializeDao();
        return dao.findByCategory(category);
    }

    public Product findById(String id) {
        initializeDao();
        return dao.findById(id);
    }

    public Product create(Product obj) {
        initializeDao();
        return this.dao.create(obj);
    }

}
