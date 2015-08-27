package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Product;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;


public class ProductDAO extends AbstractDAO<Product> {

    private static String PARAM_ID = "id";
    private static String PARAM_NAME = "productName";

    private static String QUERY_FIND_BY_ID = "FROM Product product WHERE product_id = :" + PARAM_ID;
    private static String QUERY_FIND_BY_NAME = "FROM Product product WHERE name LIKE :" + PARAM_NAME;

    public ProductDAO(SessionFactory factory) {
        super(factory);
    }

    public Product findById(String id) {
        return (Product) super.currentSession().createQuery(QUERY_FIND_BY_ID).setParameter(PARAM_ID, id).uniqueResult();
    }


    //TODO TESTEN
    public List<Product> findByName(String name){
        return super.currentSession().createQuery(QUERY_FIND_BY_NAME).setParameter(PARAM_NAME, "%"+name+"%").list();
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

    /***
     * Stores the entry in the database, if it is not existing yet
     * @param obj
     * @return the stored {@link Product}
     */
    public Product create(Product obj){
        //try to fetch the value from our storage
        Product stored = get(obj.getDatabaseId());
        if(stored == null){
            //create the value
            return persist(obj);
        }
        //value already exists
        return stored;
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
