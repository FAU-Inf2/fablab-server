package de.fau.cs.mad.fablab.rest.server.resources;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.api.SpaceApi;
import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.fau.cs.mad.fablab.rest.server.configuration.AndroidPushConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.PushFacade;
import de.fau.cs.mad.fablab.rest.server.core.spaceapi.DoorStateDAO;
import de.fau.cs.mad.fablab.rest.server.core.spaceapi.DoorStateRequest;
import de.fau.cs.mad.fablab.rest.server.core.spaceapi.remote.SpaceAPIService;
import io.dropwizard.hibernate.UnitOfWork;
import net.spaceapi.HackerSpace;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final SessionFactory mSessionFactory;
    private final SpaceApiConfiguration mConfig;
    private final DoorStateDAO mDAO;
    private final Logger mLogger;

    public SpaceAPIResource(SpaceApiConfiguration aConfig, SessionFactory aSessionFactory) {
        mSessionFactory = aSessionFactory;
        mConfig = aConfig;
        mDAO = new DoorStateDAO(aSessionFactory);
        mLogger = LoggerFactory.getLogger(SpaceAPIResource.class);
    }

    @Override
    @UnitOfWork
    public String updateDoorState(String hash, String data) {

        if (hash == null || data == null || hash.isEmpty() || data.isEmpty())
            throw new BadRequestException("no credentials provided");

        if (mConfig.getKeyFile() == null || mConfig.getKeyFile().isEmpty() ||
                mConfig.getHashAlgorithm() == null || mConfig.getHashAlgorithm().isEmpty())
            throw new ServiceUnavailableException("key file or hash algorithm is missing in configuration");

        DoorStateRequest request = DoorStateRequest.fromData(mConfig, hash, data);
        DoorState oldState = mDAO.getLastState();
        DoorState newState = request.getDoorState();

        // if there is no oldState use current state
        if (oldState == null)
            mDAO.saveState(newState);

        if (request.checkIfChanged(oldState)) {
            mLogger.info("DoorState changed, firing push event. Current state is " + newState);
            mDAO.saveState(newState);

            //TODO PUSH HERE IF DOOR OPENS
        }

        return "{\"success\":\"true\", \"state\":\"" + newState.state + "\"}";
    }

    @Override
    public HackerSpace getSpace(String name) {

        if (!name.equalsIgnoreCase(mConfig.getSpace()))
            throw new NotAllowedException("This is not allowed");

        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(mConfig.getEndpoint());
        SpaceAPIService api = WebResourceFactory.newResource(SpaceAPIService.class, target);

        return api.space();
    }
}
