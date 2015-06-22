package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.CartServer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("checkout")
public interface Checkout {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    CartServer getCart(@PathParam("id") String id);

    @POST
    @Path("/paid/{id}")
    boolean markCartAsPaid(@PathParam("id") String id);

    @POST
    @Path("/cancelled/{id}")
    boolean markCartAsCancelled(@PathParam("id") String id);
}
