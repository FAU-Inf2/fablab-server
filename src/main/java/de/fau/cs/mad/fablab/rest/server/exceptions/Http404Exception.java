package de.fau.cs.mad.fablab.rest.server.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Http404Exception extends WebApplicationException {

    public Http404Exception() {
        super(Response.Status.NOT_FOUND);
    }

    public Http404Exception(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(new ExceptionMessageWrapper(message)).type(MediaType.APPLICATION_JSON).build());
    }
}
