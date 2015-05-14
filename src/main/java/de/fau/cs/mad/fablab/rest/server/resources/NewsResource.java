package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.News;

import de.fau.cs.mad.fablab.rest.server.core.NewsDAO;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.server.core.NewsFacade;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by EE on 11.05.15.
 */

//TODO: RESPONSES like  404 and so on...
public class NewsResource implements NewsApi {

    private final NewsFacade facade;


    public NewsResource(NewsFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public News findById(long id) {
        return  this.facade.findById(id);
    }

    @UnitOfWork
    @Override
    public List<News> findAll() {
        return this.facade.findAll();
    }

    @UnitOfWork
    @Override
    public List<News> find(int offset, int limit) {
        return this.facade.find(offset, limit);
    }
}