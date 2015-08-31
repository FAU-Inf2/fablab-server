package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpException;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpInterface;
import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.jersey.DropwizardResourceConfig;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ProductDAO extends AbstractDAO<Product> {

    private static String PARAM_ID = "id";
    private static String PARAM_NAME = "productName";
    private static String PARAM_CATEGORY = "category";

    private static String QUERY_DELETE_ALL = "delete FROM Product";
    private static String QUERY_FIND_ALL = "FROM Product";
    private static String QUERY_FIND_BY_ID = "FROM Product product WHERE product_id = :" + PARAM_ID;
    private static String QUERY_FIND_BY_NAME = "FROM Product product WHERE name LIKE :" + PARAM_NAME;
    private static String QUERY_FIND_BY_CATEGORY = "FROM Product product WHERE category_string LIKE :" + PARAM_CATEGORY;

    private SessionFactory mFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDAO.class);

    public ProductDAO(SessionFactory factory) {
        super(factory);
        mFactory = factory;
        initializeDao();
    }

    /***
     * Use this method to create or update the dao store
     */
    public void initializeDao(){
        LOGGER.info("Initializing Product DAO");
        OpenErpInterface openErp = OpenErpClient.getInstance();

        Session session = mFactory.openSession();
        session.setDefaultReadOnly(false);
        session.setCacheMode(CacheMode.NORMAL);
        session.setFlushMode(FlushMode.AUTO);
        ManagedSessionContext.bind(session);

        List<Product> products;
        try {
            products = openErp.getProducts(0, 0);
            for(Product tmp : products){
                create(tmp);
            }
        } catch (OpenErpException e) {
            e.printStackTrace();
            LOGGER.error("Initializing Product DAO ERROR!");
        }
        session.flush();
        session.close();
        LOGGER.info("Initializing Product DAO SUCCESSFUL");
    }

    public Product findById(String id) {
        return (Product) super.currentSession().createQuery(QUERY_FIND_BY_ID).setParameter(PARAM_ID, id).uniqueResult();
    }

    public List<Product> findByName(String name){
        return super.currentSession().createQuery(QUERY_FIND_BY_NAME).setParameter(PARAM_NAME, "%"+name+"%").list();
    }

    public List<Product> findByCategory(String cat){
        return super.currentSession().createQuery(QUERY_FIND_BY_CATEGORY).setParameter(PARAM_CATEGORY, "%" + cat + "%").list();
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
        currentSession().createQuery(QUERY_DELETE_ALL).executeUpdate();
    }
}
