package de.fau.cs.mad.fablab.rest.server.pushservice;

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
            System.out.println("RegistrationID: " + regId.getRegistrationid());
        }
        mRegistrationIdFacade.create(regId);
        PushFacade pushFacade = new PushFacade(mPushServiceConfiguration,mSessionFactory);
        pushFacade.pushToDevice(regId,"Hinweis", "Eine neue Nachricht für: " + regId.getRegistrationid());
        pushFacade.pushToAllDevices("Hinweis", "Hat alles geklappt");


        if(regId == null){
            return Response.serverError().build();
        }

        return Response.ok().build();
    }
}
