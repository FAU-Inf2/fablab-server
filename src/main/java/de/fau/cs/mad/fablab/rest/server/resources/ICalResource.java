package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.server.core.*;

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

    private final ICalDAO dao;

    public ICalResource(ICalDAO dao) {
        this.dao = dao;
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @Override
    public ICal findById(@PathParam("id")long id) {
        return dao.findById(id);
    }

    @GET
    @UnitOfWork
    @Override
    public List<ICal> findAll() {
        return dao.findAll();
    }

    @POST
    @UnitOfWork
    @Override
    public ICal create(ICal obj) {
        return dao.create(obj);
    }


    @PUT
    @UnitOfWork
    @Override
    public ICal update(ICal obj) {
       return dao.update(obj);
    }
    @DELETE
    @UnitOfWork
    @Path("/{id}")
    @Override
    public void delete(@PathParam("id") long id) {
        dao.delete(id);
    }

    @DELETE
    @UnitOfWork
    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}