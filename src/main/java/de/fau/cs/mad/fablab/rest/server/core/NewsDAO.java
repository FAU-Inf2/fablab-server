package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.News;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by EE on 11.05.15.
 */
public class NewsDAO extends AbstractDAO<News> {

    public NewsDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public News findById(long id) {
        return super.get(id);
    }

    @SuppressWarnings("unchecked")
    public List<News> findAll() {
        return super.currentSession().createQuery("FROM News").list();
    }


    @SuppressWarnings("unchecked")
    public List<News> find(int offset, int limit) {
        Query q = currentSession().createQuery("FROM News");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.list();
    }


    //Create
    public News create(News obj){
        return persist(obj);
    }


    //Update
    public News update(News modified) {
        News stored = this.get(modified.getId());
        stored.setCategory(modified.getCategory());
        stored.setCreator(modified.getCreator());
        stored.setDescription(modified.getDescription());
        stored.setDescriptionShort(modified.getDescriptionShort());
        stored.setIsPermaLink(modified.getIsPermaLink());
        stored.setLink(modified.getLink());
        stored.setLinkToPreviewImage(modified.getLinkToPreviewImage());
        stored.setTitle(modified.getTitle());
        stored.setPubDate(modified.getPubDate());
        this.persist(stored);
        return stored;
    }


    //Delete
    public boolean delete(long id) {
        System.out.println("DELETED: " + String.valueOf(id));
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        System.out.println("DELETED: ALL!!! ");
        currentSession().createQuery("delete FROM News").executeUpdate();
    }
}
