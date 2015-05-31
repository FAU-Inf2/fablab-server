package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.Cart;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("carts")
public interface Checkout {
    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    Cart getCurrentCart();
}
