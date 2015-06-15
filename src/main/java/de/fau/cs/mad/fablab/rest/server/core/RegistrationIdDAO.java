package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;


public class RegistrationIdDAO extends AbstractDAO<RegistrationId> {

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
        Query query = currentSession().createQuery("FROM RegistrationId reg WHERE reg.registrationid LIKE " + aRegistrationId.getRegistrationid());
        if((query.list().size() == 0)){
            return false;
        }
        return true;
    }

    public List<RegistrationId> findAll(){
        Query query = currentSession().createQuery("FROM RegistrationId");
        return query.list();
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
        currentSession().createQuery("delete FROM RegistrationId").executeUpdate();
    }
}
