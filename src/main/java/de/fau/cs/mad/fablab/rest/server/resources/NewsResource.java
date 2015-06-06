package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.api.NewsApi;
import de.fau.cs.mad.fablab.rest.server.core.NewsFacade;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.InternalServerErrorException;
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
        News result = this.facade.findById(id);
        if (result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<News> findAll() {
        List<News> result = this.facade.findAll();
        if (result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<News> find(int offset, int limit) {
        List<News> result = this.facade.find(offset, limit);
        if (result == null){
            throw new InternalServerErrorException("There is a problem getting the results");
        }
        return result;
    }
}