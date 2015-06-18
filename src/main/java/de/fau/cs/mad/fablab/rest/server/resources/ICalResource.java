package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ICalApi;
import de.fau.cs.mad.fablab.rest.core.ICal;

import de.fau.cs.mad.fablab.rest.server.core.ICalFacade;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import java.util.List;


public class ICalResource implements ICalApi {

    private final ICalFacade facade;


    public ICalResource(ICalFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public ICal findById(long id) {
        ICal result = this.facade.findById(id);
        if (result == null){
            throw new NotFoundException("There is no Event with id " + id);
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<ICal> findAll() {
        List<ICal> result = this.facade.findAll();
        if (result == null){
            throw new InternalServerErrorException("An error occurred while updating the Event-list");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<ICal> find(int offset, int limit) {
        List<ICal> result = this.facade.find(offset, limit);
        if (result == null){
            throw new InternalServerErrorException("An error occurred while updating the Event-list");
        }
        return result;
    }

}