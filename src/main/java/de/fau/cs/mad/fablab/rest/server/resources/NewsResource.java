package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.server.core.NewsFacade;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
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
            throw new NotFoundException("There is no News article with id " + id);
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<News> findAll() {
        List<News> result = this.facade.findAll();
        if (result == null){
            throw new InternalServerErrorException("An error occurred while updating the News-list");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<News> find(int offset, int limit) {
        List<News> result = this.facade.find(offset, limit);
        if (result == null){
            throw new InternalServerErrorException("An error occurred while updating the News-list");
        }
        return result;
    }
}