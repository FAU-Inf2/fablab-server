package de.fau.cs.mad.fablab.rest.server.exceptions;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Http503Exception extends WebApplicationException {

    public Http503Exception() {
        super(Response.Status.SERVICE_UNAVAILABLE);
    }

    public Http503Exception(String message) {
        super(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(new ExceptionMessageWrapper(message)).type(MediaType.APPLICATION_JSON).build());
    }
}
