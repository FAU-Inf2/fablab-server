package de.fau.cs.mad.fablab.rest.server.core;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.drupal.NewsClient;
import de.fau.cs.mad.fablab.rest.server.drupal.NewsInterface;

import java.util.List;

/**
 * Created by EE on 14.05.15.
 */


public class NewsFacade {

    private final NewsDAO dao;
    private final NewsInterface newsInterface;

    public NewsFacade(NewsDAO dao) {
        this.dao = dao;
        this.newsInterface = NewsClient.getInstance();
    }

    public News findById(Long id) {
        return this.dao.findById(id);
        //return newsInterface.findById(id);
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
