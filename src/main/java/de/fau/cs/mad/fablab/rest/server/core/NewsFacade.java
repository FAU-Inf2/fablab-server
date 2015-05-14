package de.fau.cs.mad.fablab.rest.server.core;
import de.fau.cs.mad.fablab.rest.core.News;

import java.util.List;

/**
 * Created by EE on 14.05.15.
 */


public class NewsFacade {

    private final NewsDAO dao;

    public NewsFacade(NewsDAO dao) {
        this.dao = dao;
    }

    public News findById(Long id) {
        return this.dao.findById(id);
    }

    public List<News> findAll(){
        return this.dao.findAll();
    }

    public List<News> find(int offset, int limit) {
        return this.dao.find(offset, limit);
    }

    public News create(News obj) {
        return this.dao.create(obj);
    }

}
