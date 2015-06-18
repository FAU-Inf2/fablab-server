package de.fau.cs.mad.fablab.rest.server.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Http400Exception extends WebApplicationException {

    public Http400Exception() {
        super(Response.Status.BAD_REQUEST);
    }

    public Http400Exception(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionMessageWrapper(message)).type(MediaType.APPLICATION_JSON).build());
    }
}
