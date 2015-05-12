package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */
@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource implements ProductApi {

    private final ProductDAO dao;

    public ProductResource(ProductDAO dao) {
        this.dao = dao;
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @Override
    public Product findById(@PathParam("id")long id) {
        return dao.findById(id);
    }

    @GET
    @UnitOfWork
    @Override
    public List<Product> findAll() {
        return dao.findAll();
    }

    @POST
    @UnitOfWork
    @Override
    public Product create(Product obj) {
        return dao.create(obj);
    }


    @PUT
    @UnitOfWork
    @Override
    public Product update(Product obj) {
       return dao.update(obj);
    }
    @DELETE
    @UnitOfWork
    @Path("/{id}")
    @Override
    public void delete(@PathParam("id") long id) {
        dao.delete(id);
    }

    @DELETE
    @UnitOfWork
    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}