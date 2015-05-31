package de.fau.cs.mad.fablab.rest.server.pushservice;

import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import de.fau.cs.mad.fablab.rest.server.core.RegistrationIdDAO;
import de.fau.cs.mad.fablab.rest.server.core.RegistrationIdFacade;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jersey.sessions.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PushFacade {

    private SessionFactory mSessionFactory;

    public PushFacade(SessionFactory aSessionFactory){
        mSessionFactory = aSessionFactory;
    }


    public void push(String aMessage){
        PushContent content = new PushContent();
        RegistrationIdFacade registrationIdFacade = new RegistrationIdFacade(new RegistrationIdDAO(mSessionFactory));
        List<RegistrationId> registrationIds = registrationIdFacade.findAll();
        for(RegistrationId registrationId : registrationIds){
            content.addRegId(registrationId.getRegistrationid());
        }
        content.createData("Hinweis",aMessage);
        AndroidPushService pushService = new AndroidPushService();
        try {
            pushService.pushJson("API_KEY", content);
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}
