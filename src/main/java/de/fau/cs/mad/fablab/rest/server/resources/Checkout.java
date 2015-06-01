package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.Cart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("cart")
public interface Checkout {

    @GET
    @Path("/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    Cart getCart(String id);

    @POST
    @Path("/checkout/paid")
    void markCartAsPaid(String id);

    @POST
    @Path("/checkout/cancelled")
    void markCartAsCancelled(String id);
}
