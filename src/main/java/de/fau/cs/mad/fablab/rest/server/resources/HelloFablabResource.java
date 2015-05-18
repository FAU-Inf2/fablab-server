package de.fau.cs.mad.fablab.rest.server.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.entities.WelcomeUser;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpException;
import de.fau.cs.mad.fablab.rest.server.openerp.OpenErpInterface;
import io.dropwizard.jersey.params.BooleanParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Resource class for our /hello-fablab uri
 * NOTE: This class is being used by multiple threads concurrently
 */
@Path("/hello-fablab")
@Produces(MediaType.APPLICATION_JSON)
public class HelloFablabResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final AtomicLong greetCounter;

    OpenErpInterface mOpenErp;

    public HelloFablabResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        greetCounter = new AtomicLong();

        try {
            mOpenErp = new OpenErpClient();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Timed
    public WelcomeUser welcomeUser(@QueryParam("name") Optional<String> name) {
        final String nameVal = String.format(template, name.or(defaultName));
        return new WelcomeUser(counter.incrementAndGet(), nameVal, greetCounter.get());
    }

    @POST
    @Timed
    public WelcomeUser greetOtherUsers(@QueryParam("greet") BooleanParam greet,
                                       @QueryParam("name") Optional<String> name) {
        final String nameVal = String.format(template, name.or(defaultName));
        if (greet.get()) {
            greetCounter.incrementAndGet();
        }
        return new WelcomeUser(counter.incrementAndGet(), nameVal, greetCounter.get());
    }

    /**
     * Search and return products which contain the search string in their name
     * @param name the search string
     * @param max  the max number of elements returned
     * @return a list of {@link Product} entries
     */
    @GET
    @Path("/products/search")
    public Response searchProductByName(@QueryParam("name") String name,
                                             @QueryParam("max") int max) {
        try {
            return Response.ok(mOpenErp.searchForProducts(name, max, 0)).build();
        } catch (OpenErpException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).build();
        }
    }

    /***
     * Receives a list of {@link Product} from a given openerp instance
     * @param max the max number of elements returned
     * @param offset the offset where to start
     * @return a list of {@link Product} entries
     */
    @GET
    @Path("/products")
    public Response getProducts(@QueryParam("max") int max,
                                     @QueryParam("offset") int offset) {
        try {
            return Response.ok(mOpenErp.getProducts(max, offset)).build();
        } catch (OpenErpException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
