package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by EE on 12.05.15.
 */
@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource implements CartApi {

    private final CartDAO dao;

    public CartResource(CartDAO dao) {
        this.dao = dao;
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @Override
    public Cart findById(@PathParam("id")long id) {
        return dao.findById(id);
    }

    @GET
    @UnitOfWork
    @Override
    public List<Cart> findAll() {
        return dao.findAll();
    }

    @POST
    @UnitOfWork
    @Override
    public Cart create(Cart obj) {
        return dao.create(obj);
    }


    @PUT
    @UnitOfWork
    @Override
    public Cart update(Cart obj) {
       return dao.update(obj);
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    @Override
    public void delete(@PathParam("id") long id) {
        dao.delete(id);
    }

    @POST
    @UnitOfWork
    @Path("/addProduct/{cartid}/{productid}/{amount}")
    public Cart addProduct(@PathParam("cartid") long cartId, @PathParam("productid") long productId, @PathParam("amount") double amount){
        //TODO -> GET PRODUCT BY PRODUCTID FROM PRODUCT LIST/OPENERP?!
        Product product = new Product();
        product.setId(productId);

        return dao.addProduct(cartId, product, amount);
    }
}