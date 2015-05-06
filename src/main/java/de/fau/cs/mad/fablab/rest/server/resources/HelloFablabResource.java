package de.fau.cs.mad.fablab.rest.server.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import de.fau.cs.mad.fablab.rest.entities.WelcomeUser;
import io.dropwizard.jersey.params.BooleanParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

/***
 * Resource class for our /hello-fablab uri
 * NOTE: This class is being used by multiple threads concurrently
 */
@Path("/hello-fablab")
@Produces(MediaType.APPLICATION_JSON)
public class HelloFablabResource
{
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final AtomicLong greetCounter;

    public HelloFablabResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        greetCounter = new AtomicLong();
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
                                       @QueryParam("name") Optional<String> name){
        final String nameVal = String.format(template, name.or(defaultName));
        if(greet.get()) greetCounter.incrementAndGet();
        return new WelcomeUser(counter.incrementAndGet(), nameVal, greetCounter.get());
    }
}
