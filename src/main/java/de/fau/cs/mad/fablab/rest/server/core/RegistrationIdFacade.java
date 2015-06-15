package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import org.hibernate.Query;

import java.util.List;

public class RegistrationIdFacade {

    private RegistrationIdDAO mRegistrationIdDAO;

    public RegistrationIdFacade(RegistrationIdDAO aRegistrationIdDAO){
        mRegistrationIdDAO = aRegistrationIdDAO;
    }

    public RegistrationId create(RegistrationId aRegistrationId){
        return mRegistrationIdDAO.create(aRegistrationId);
    }

    public RegistrationId update(RegistrationId aModifiedRegistrationId){
        return mRegistrationIdDAO.update(aModifiedRegistrationId);
    }

    public boolean alreadyExists(RegistrationId aRegistrationId){
        return mRegistrationIdDAO.alreadyExists(aRegistrationId);
    }

    public List<RegistrationId> findAll(){
        return mRegistrationIdDAO.findAll();    }

    public boolean delete(long registrationId_id) {
        return mRegistrationIdDAO.delete(registrationId_id);
    }

    public void deleteAll(){
        mRegistrationIdDAO.deleteAll();
    }
}
