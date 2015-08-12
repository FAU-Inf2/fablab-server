package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;


public class RegistrationIdDAO extends AbstractDAO<RegistrationId> {

    public static final String TABLE_NAME = "RegistrationId";

    public RegistrationIdDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public RegistrationId create(RegistrationId aRegistrationId){
        return persist(aRegistrationId);
    }

    public RegistrationId update(RegistrationId aModifiedRegistrationId){
        RegistrationId storedRegistrationId = get(aModifiedRegistrationId.getRegistrationid_id());
        storedRegistrationId.setRegistrationid(aModifiedRegistrationId.getRegistrationid());
        persist(storedRegistrationId);
        return storedRegistrationId;
    }

    public boolean alreadyExists(RegistrationId aRegistrationId){
        Query query = currentSession().createQuery("FROM " + TABLE_NAME + " reg WHERE reg.registrationid LIKE :regId");
        query.setParameter("regId", aRegistrationId.getRegistrationid());

        if((query.list().size() == 0)){
            return false;
        }
        return true;
    }

    public List<RegistrationId> findAll(){
        Query query = currentSession().createQuery("FROM " + TABLE_NAME);
        return query.list();
    }

    public boolean delete(String registrationId) {
        Query query = currentSession().createQuery("FROM " + TABLE_NAME + " reg WHERE reg.registrationid LIKE :regId");
        query.setParameter("regId", registrationId);

        if((query.list().size() == 0)){
            return false;
        }
        for (Object regId : query.list()) {
            currentSession().delete(regId);
        }
        return true;
    }

    public boolean delete(long registrationId_id) {
        System.out.println("DELETED: " + String.valueOf(registrationId_id));
        if (get(registrationId_id) == null)
            return false;

        currentSession().delete(get(registrationId_id));
        return true;
    }

    public void deleteAll(){
        System.out.println("DELETED: ALL!!! ");
        currentSession().createQuery("DELETE FROM " + TABLE_NAME).executeUpdate();
    }
}
