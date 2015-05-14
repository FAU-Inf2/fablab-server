package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Cart;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import de.fau.cs.mad.fablab.rest.core.Product;
import java.util.List;

/**
 * Created by EE on 11.05.15.
 */
public class CartDAO extends AbstractDAO<Cart> {

    public CartDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public Cart findById(long id) {
        return super.get(id);
    }

    @SuppressWarnings("unchecked")
    public List<Cart> findAll() {
        return super.currentSession().createQuery("FROM Cart").list();
    }

    //Create
    public Cart create(Cart obj){
        return persist(obj);
    }


    //Update
    public Cart update(Cart modified) {
        Cart stored = this.get(modified.getId());
        stored.setProducts(modified.getProducts());
        stored.setStatus(stored.getStatus());
        this.persist(stored);
        return stored;
    }


    public Cart addProduct(long id, Product product, double amount){
        Cart c = findById(id);
        c.addProduct(product, amount);
        return c;
    }

    public Cart changeProductAmount(long id, Product product, double newAmount){
        Cart c = findById(id);
        c.removeProduct(product);
        c.addProduct(product, newAmount);
        return c;
    }


    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

}
