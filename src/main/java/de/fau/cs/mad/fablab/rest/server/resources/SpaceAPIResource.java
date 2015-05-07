package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.server.remote.SpaceAPIService;
import de.fau.cs.mad.fablab.rest.api.SpaceApi;

import net.spaceapi.HackerSpace;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

/***
 * Resource class for our /spaceapi uri
 * NOTE: This class is being used by multiple threads concurrently
 */
public class SpaceAPIResource implements SpaceApi
{
    private final String endpoint;

    public SpaceAPIResource(String endpoint) {
        this.endpoint = endpoint;
    }

    public HackerSpace getSpace(String name) {

        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(endpoint);
        SpaceAPIService api = WebResourceFactory.newResource(SpaceAPIService.class, target);

        return api.space(name);
    }
}
