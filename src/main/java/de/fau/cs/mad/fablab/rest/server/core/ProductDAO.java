package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Product;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */

public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(SessionFactory factory) {
        super(factory);
    }


    public Product findById(long id) {
        return super.get(id);
    }


    //TODO TESTEN
    public List<Product> findByName(String name){
        List<Product> products = super.currentSession().createQuery("FROM Product").list();
        List<Product> foundProducts = new ArrayList<Product>();

        if(products != null) {
            for (Product p : products) {
                if(p.getName().contains(name))
                    foundProducts.add(p);
            }
        }
        return foundProducts;
    }

    //TODO TESTEN
    public List<Product> findByCategory(String cat){
        List<Product> products = super.currentSession().createQuery("FROM Product").list();
        List<Product> foundProducts = new ArrayList<Product>();

        if(products != null) {
            for (Product p : products) {
                if(p.getCategoryString().contains(cat))
                    foundProducts.add(p);
            }
        }
        return foundProducts;
    }


    @SuppressWarnings("unchecked")
    public List<Product> findAll() {
        return super.currentSession().createQuery("FROM Product").list();
    }

    //Create
    public Product create(Product obj){
        return persist(obj);
    }


    //Update
    public Product update(Product modified) {
        Product stored = this.get(modified.getProductId());
        stored.setProductId(modified.getProductId());
        stored.setName(modified.getName());
        stored.setPrice(modified.getPrice());
        stored.setDescription(modified.getDescription());
        stored.setCategoryId(modified.getCategoryId());
        stored.setCategoryString(modified.getCategoryString());
        this.persist(stored);
        return stored;
    }


    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        currentSession().createQuery("delete FROM Product").executeUpdate();
    }
}
