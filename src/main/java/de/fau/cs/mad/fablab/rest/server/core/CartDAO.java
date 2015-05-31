package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Cart;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import de.fau.cs.mad.fablab.rest.core.Product;
import java.util.List;

public class CartDAO extends AbstractDAO<Cart> {

    public static long lastPushed = -1;

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
        lastPushed = obj.getId();
        return persist(obj);
    }


    //Update
    public Cart update(Cart modified) {
        Cart stored = this.get(modified.getId());
        stored.setProducts(modified.getProducts());
        stored.setStatus(stored.getStatus());
        this.persist(stored);
        lastPushed = modified.getId();
        return stored;
    }


    //Not used so far...
    public Cart addProduct(long id, Product product, double amount){
        Cart c = findById(id);
        c.addProduct(product, amount);
        return c;
    }

    //Not used so far...
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
        if(id == lastPushed)
            lastPushed = -1;
        return true;
    }

    public Cart getCurrentCart(){
        if(lastPushed != -1)
            return super.get(lastPushed);
        return null;
    }

}
