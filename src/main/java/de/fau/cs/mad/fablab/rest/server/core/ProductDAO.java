package de.fau.cs.mad.fablab.rest.server.core;


import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by EE on 12.05.15.
 */
public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public Product findById(long id) {
        Product n = super.get(id);
        if(n == null)
            return new Product();
        return n;
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
        Product stored = this.get(modified.getId());
        stored.setName(modified.getAvailable());
        stored.setDescription(modified.getDescription());
        stored.setAvailable(modified.getAvailable());
        stored.setPrice(modified.getPrice());
        stored.setUnit(modified.getUnit());
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
