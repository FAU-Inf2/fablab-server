package de.fau.cs.mad.fablab.rest.server.resources;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.api.SpaceApi;
import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import de.fau.cs.mad.fablab.rest.server.core.doorstate.DoorStateRequest;
import de.fau.cs.mad.fablab.rest.server.remote.SpaceAPIService;
import net.spaceapi.HackerSpace;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/***
 * Resource class for our /spaceapi uri
 * NOTE: This class is being used by multiple threads concurrently
 */
public class SpaceAPIResource implements SpaceApi
{
    private final SpaceApiConfiguration config;

    public SpaceAPIResource(SpaceApiConfiguration config) {
        this.config = config;
    }

    @Override
    public String updateDoorState(String hash, String data) {

        if (hash == null || data == null || hash.isEmpty() || data.isEmpty())
            throw new BadRequestException("no credentials provided");

        if (config.getKeyFile() == null || config.getKeyFile().isEmpty() ||
                config.getHashAlgorithm() == null || config.getHashAlgorithm().isEmpty())
            throw new ServiceUnavailableException("keyfile or hash algorithm is missing in configuration");

        DoorStateRequest request = DoorStateRequest.fromData(config, hash, data);

        // now we have to fire push event

        return "{\"success\":\"true\", \"state\":\"" + request.getDoorState().state + "\"}";
    }

    @Override
    public HackerSpace getSpace(String name) {

        if (!name.equalsIgnoreCase(config.getSpace()))
            throw new NotAllowedException("This is not allowed");

        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(config.getEndpoint());
        SpaceAPIService api = WebResourceFactory.newResource(SpaceAPIService.class, target);

        return api.space();
    }
}
