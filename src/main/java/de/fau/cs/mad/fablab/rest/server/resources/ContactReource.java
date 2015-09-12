package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.api.ContactApi;
import de.fau.cs.mad.fablab.rest.core.CartEntryServer;
import de.fau.cs.mad.fablab.rest.core.CartServer;
import de.fau.cs.mad.fablab.rest.core.CartStatus;


import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ContactReource implements ContactApi{

    @Override
    public Response sendFeedback(String aMessage) {
        System.out.println("Send Feedbac: String: " + aMessage);
        if(aMessage.isEmpty()) {
            return Response.serverError().build();
        }
        return Response.ok().build();
    }


    @Override
    public Response sendErrorMessage(long aToolId, String aMessage) {
        System.out.println("Send ErrorMessage: String: " + aToolId + " and " + aMessage);
        if(aToolId == 0){
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}
