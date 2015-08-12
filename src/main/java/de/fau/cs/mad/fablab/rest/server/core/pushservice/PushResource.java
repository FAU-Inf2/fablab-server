package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.api.PushApi;
import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import de.fau.cs.mad.fablab.rest.server.configuration.PushServiceConfiguration;
import de.fau.cs.mad.fablab.rest.server.core.RegistrationIdDAO;
import de.fau.cs.mad.fablab.rest.server.core.RegistrationIdFacade;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;

import javax.ws.rs.core.Response;


public class PushResource implements PushApi{

    private RegistrationIdFacade mRegistrationIdFacade;
    private SessionFactory mSessionFactory;
    private PushServiceConfiguration mPushServiceConfiguration;

    public PushResource(PushServiceConfiguration aPushServiceConfiguration,SessionFactory aSessionFactory){
        mRegistrationIdFacade = new RegistrationIdFacade(new RegistrationIdDAO(aSessionFactory));
        mPushServiceConfiguration = aPushServiceConfiguration;
        mSessionFactory = aSessionFactory;
    }

    @UnitOfWork
    @Override
    public Response addRegistrationId(RegistrationId regId) {
        if(regId == null) {
            System.out.println("RegistrationID is null");
            return Response.serverError().build();
        }
        if(!mRegistrationIdFacade.alreadyExists(regId)){
            mRegistrationIdFacade.create(regId);
        }

        return Response.ok().build();
    }

    @Override
    @UnitOfWork
    public Response removeRegistrationId(RegistrationId aRegistrationId) {
        if (mRegistrationIdFacade.delete(aRegistrationId.getRegistrationid())) {
            return Response.ok().build();
        }
        return Response.serverError().build();
    }
}
