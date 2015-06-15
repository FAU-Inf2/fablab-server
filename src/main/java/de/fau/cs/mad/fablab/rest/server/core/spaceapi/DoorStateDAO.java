package de.fau.cs.mad.fablab.rest.server.core.spaceapi;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * DAO for DoorState
 */
public class DoorStateDAO extends AbstractDAO<DoorState> {

    public static final String TABLE_NAME = "DoorState";

    public DoorStateDAO(SessionFactory factory) {
        super(factory);
    }

    public void saveState(DoorState state) {
        persist(state);
    }

    public DoorState getLastState() {
        Query q = super.currentSession().createQuery("FROM " + TABLE_NAME + " ORDER BY time DESC");
        q.setFirstResult(0);
        q.setMaxResults(1);

        List<DoorState> list = list(q);

        if (list.size() == 0)
            return null;

        return list.get(0);
    }

}
