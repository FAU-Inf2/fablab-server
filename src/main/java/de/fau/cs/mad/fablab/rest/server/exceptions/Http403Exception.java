package de.fau.cs.mad.fablab.rest.server.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Http403Exception extends WebApplicationException {

    public Http403Exception() {
        super(Response.Status.FORBIDDEN);
    }

    public Http403Exception(String message) {
        super(Response.status(Response.Status.FORBIDDEN).entity(new ExceptionMessageWrapper(message)).type(MediaType.APPLICATION_JSON).build());
    }
}
