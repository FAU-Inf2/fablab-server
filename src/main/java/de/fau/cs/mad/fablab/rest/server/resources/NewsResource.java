package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.server.core.NewsFacade;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http400Exception;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http404Exception;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http500Exception;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.Date;
import java.util.List;


public class NewsResource implements NewsApi {

    private final NewsFacade facade;


    public NewsResource(NewsFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public News findById(long id) {
        News result = this.facade.findById(id);
        if (result == null) {
            throw new Http404Exception("There is no News article with id " + id);
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<News> findAll() {
        List<News> result = this.facade.findAll();
        if (result == null){
            throw new Http500Exception("An error occurred while updating the News-list");
        }
        if (result.size() == 0) throw new Http404Exception("Result is empty");
        return result;
    }

    @UnitOfWork
    @Override
    public Long lastUpdate() {
        return this.facade.lastUpdate();
    }

    @UnitOfWork
    @Override
    public List<News> find(int offset, int limit) {
        if (offset == 0 && limit == 0) return findAll();
        if (offset < 0 || limit < 0) throw new Http400Exception("offset < 0 or limit < 0 is not permitted");

        List<News> result = this.facade.find(offset, limit);
        if (result == null){
            throw new Http404Exception("offset " + offset + " is out of bounds!");
        }
        if (result.size() == 0) throw new Http404Exception("Result is empty");
        return result;
    }
}