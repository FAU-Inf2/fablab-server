package de.fau.cs.mad.fablab.rest.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.ProductDAO;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpException;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpInterface;
import io.dropwizard.servlets.tasks.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.List;

public class UpdateProductDatabaseTask extends Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateProductDatabaseTask.class);
    private ProductDAO dao;
    private SessionFactory mFactory;

    public UpdateProductDatabaseTask(SessionFactory factory) {
        super("UpdateProductDatabaseTask");
        mFactory = factory;
        dao = new ProductDAO(factory);
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        final Session session = mFactory.openSession();
        try {
            ManagedSessionContext.bind(session);
            Transaction transaction = session.beginTransaction();
            try {
                LOGGER.info("Initializing Product DAO");
                initializeDao();
                transaction.commit();
                LOGGER.info("Initializing Product DAO SUCCESSFUL");
            } catch (Exception e) {
                transaction.rollback();
                LOGGER.error("Initializing Product DAO ERROR!");
                throw new RuntimeException("Unable to update Product Database \n\n"+e.getMessage());
            }
        } finally {
            session.close();
            ManagedSessionContext.unbind(mFactory);
        }
    }

    /***
     * Use this method to create or update the dao store
     */
    private void initializeDao(){
        OpenErpInterface openErp = OpenErpClient.getInstance();
        List<Product> products;
        try {
            products = openErp.getProducts(0, 0);
            for(Product tmp : products){
                dao.create(tmp);
            }
        } catch (OpenErpException e) {
            e.printStackTrace();
        }
    }
}
