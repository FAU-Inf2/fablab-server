package de.fau.cs.mad.fablab.rest.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.server.core.ProductDAO;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpException;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpInterface;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.servlets.tasks.Task;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.List;

public class UpdateProductDatabaseTask extends Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateProductDatabaseTask.class);
    private ProductDAO dao;

    public UpdateProductDatabaseTask(SessionFactory factory) {
        super("UpdateProductDatabaseTask");
        dao = new ProductDAO(factory);
    }

    @UnitOfWork
    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        initializeDao();
    }

    /***
     * Use this method to create or update the dao store
     */
    private void initializeDao(){
        LOGGER.info("Initializing Product DAO");
        OpenErpInterface openErp = OpenErpClient.getInstance();
        List<Product> products;
        try {
            products = openErp.getProducts(0, 0);
            for(Product tmp : products){
                dao.create(tmp);
            }
        } catch (OpenErpException e) {
            e.printStackTrace();
            LOGGER.error("Initializing Product DAO ERROR!");
        }
        LOGGER.info("Initializing Product DAO SUCCESSFUL");
    }
}
