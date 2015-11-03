package de.fau.cs.mad.fablab.rest.server.exceptions;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Http500Exception extends WebApplicationException {

    public Http500Exception() {
        super(Response.Status.INTERNAL_SERVER_ERROR);
    }

    public Http500Exception(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionMessageWrapper(message)).type(MediaType.APPLICATION_JSON).build());
    }
}
