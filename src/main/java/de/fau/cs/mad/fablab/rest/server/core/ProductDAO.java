package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Product;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.*;
import java.util.List;


public class ProductDAO extends AbstractDAO<Product> {

    private static String PARAM_ID = "id";
    private static String PARAM_NAME = "productName";
    private static String PARAM_CATEGORY = "category";

    private static String QUERY_DELETE_ALL = "delete FROM Product";
    private static String QUERY_FIND_ALL = "FROM Product";
    private static String QUERY_FIND_BY_ID = "FROM Product product WHERE product_id = :" + PARAM_ID;
    private static String QUERY_FIND_BY_NAME = "FROM Product product WHERE upper(name) LIKE ?";

    private static String QUERY_FIND_BY_CATEGORY = "FROM Product product WHERE upper(category_string) LIKE :" + PARAM_CATEGORY;

    public ProductDAO(SessionFactory factory) {
        super(factory);
    }

    public Product findById(String id) {
        return (Product) super.currentSession().createQuery(QUERY_FIND_BY_ID).setParameter(PARAM_ID, id).uniqueResult();
    }

    public List<Product> findByName(String name){
        String[] searchTokens = name.split(" ");
        String queryString = QUERY_FIND_BY_NAME;

        if(searchTokens.length > 1){
            for(int i = 1; i < searchTokens.length; i++){
                queryString += " and upper(name) LIKE ?";
            }
        }
        Query query = super.currentSession().createQuery(queryString);
        for(int i = 0; i < searchTokens.length; i++){
            query.setParameter(i, "%"+searchTokens[i].toUpperCase()+"%");
        }

        return query.list();
    }

    public List<Product> findByCategory(String cat){
        return super.currentSession().createQuery(QUERY_FIND_BY_CATEGORY).setParameter(PARAM_CATEGORY, "%" + cat.toUpperCase() + "%").list();
    }

    public List<Product> findAll() {
        return super.currentSession().createQuery(QUERY_FIND_ALL).list();
    }

    /***
     * Stores the entry in the database, if it is not existing yet
     * @param obj
     * @return the stored {@link Product}
     */
    public Product create(Product obj){
        return persist(obj);
    }
    
    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        super.currentSession().createQuery(QUERY_DELETE_ALL).executeUpdate();
    }
}
