package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.PushApi;
import de.fau.cs.mad.fablab.rest.core.PushToken;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.PushFacade;

import javax.ws.rs.core.Response;


public class PushResource implements PushApi{

    @Override
    public Response subscribeDoorOpensNextTime(PushToken pushToken) {
        PushFacade.getInstance().subscribeDoorOpensNextTime(pushToken);
        return Response.ok().build();
    }

    @Override
    public Response unsubscribeDoorOpensNextTime(PushToken pushToken) {
        PushFacade.getInstance().unsubscribeDoorOpensNextTime(pushToken);
        return Response.ok().build();
    }
}
