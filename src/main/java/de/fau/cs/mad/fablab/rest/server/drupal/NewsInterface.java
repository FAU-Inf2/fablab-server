package de.fau.cs.mad.fablab.rest.server.drupal;

import de.fau.cs.mad.fablab.rest.core.News;
import java.util.List;

public interface NewsInterface {
    public News findById(long id);
    public List<News> find(int offset, int limit);
    public List<News> findAll();
}
