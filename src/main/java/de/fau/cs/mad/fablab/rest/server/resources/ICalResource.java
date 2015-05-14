package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ICalApi;
import de.fau.cs.mad.fablab.rest.core.ICal;

import de.fau.cs.mad.fablab.rest.server.core.ICalFacade;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by EE on 11.05.15.
 */
@Path("/ical")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ICalResource implements ICalApi {

    private final ICalFacade facade;


    public ICalResource(ICalFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public ICal findById(long id) {
        return  this.facade.findById(id);
    }

    @UnitOfWork
    @Override
    public List<ICal> findAll() {
        return this.facade.findAll();
    }

}