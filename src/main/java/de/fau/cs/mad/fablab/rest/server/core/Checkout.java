package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.Cart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("cart")
public interface Checkout {

    @GET
    @Path("/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    Cart getCart(@QueryParam("id") String id);

    @POST
    @Path("/checkout/paid")
    void markCartAsPaid(@QueryParam("id") String id);

    @POST
    @Path("/checkout/cancelled")
    void markCartAsCancelled(@QueryParam("id") String id);
}
