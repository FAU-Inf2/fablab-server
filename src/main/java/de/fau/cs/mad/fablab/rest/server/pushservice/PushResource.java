package de.fau.cs.mad.fablab.rest.server.pushservice;

import ch.qos.logback.core.net.SyslogOutputStream;
import de.fau.cs.mad.fablab.rest.api.PushApi;
import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import de.fau.cs.mad.fablab.rest.entities.WelcomeUser;
import de.fau.cs.mad.fablab.rest.server.core.RegistrationIdDAO;
import de.fau.cs.mad.fablab.rest.server.core.RegistrationIdFacade;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;


public class PushResource implements PushApi{

    private RegistrationIdFacade mRegistrationIdFacade;

    public PushResource(RegistrationIdFacade aRegistrationIdFacade){
        mRegistrationIdFacade = aRegistrationIdFacade;
    }

    @UnitOfWork
    @Override
    public Response addRegistrationId(RegistrationId regId) {
        if(regId == null) {
            System.out.println("RegistrationID: " + regId.getRegistrationid());
        }
        mRegistrationIdFacade.create(new RegistrationId("112341234"));
        List<RegistrationId> registrationIdList = mRegistrationIdFacade.findAll();

        for(RegistrationId registrationId : registrationIdList){
            System.out.println(registrationId.getRegistrationid_id());
        }

        long l = 1;
        if(regId == null){
            return Response.serverError().build();
        }

        return Response.ok().build();
    }
}
