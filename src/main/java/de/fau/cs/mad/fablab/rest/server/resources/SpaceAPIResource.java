package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.server.remote.SpaceAPIService;
import de.fau.cs.mad.fablab.rest.api.SpaceApi;

import net.spaceapi.HackerSpace;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
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
    private final String spaceName;

    public SpaceAPIResource(String endpoint, String spaceName) {
        this.spaceName = spaceName;
        this.endpoint = endpoint;
    }

    @Override
    public String updateDoorState(String key, String data) {

        if (key.isEmpty() || data.isEmpty())
            throw new NotAuthorizedException("no credentials provided");


        return "{success:true}";
    }

    @Override
    public HackerSpace getSpace(String name) {

        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(endpoint);
        SpaceAPIService api = WebResourceFactory.newResource(SpaceAPIService.class, target);

        if (!name.equalsIgnoreCase(spaceName))
            throw new NotAllowedException("This is not allowed");

        return api.space(name);
    }
}
