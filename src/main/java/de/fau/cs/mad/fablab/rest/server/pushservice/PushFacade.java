package de.fau.cs.mad.fablab.rest.server.pushservice;

import de.fau.cs.mad.fablab.rest.core.RegistrationId;
import de.fau.cs.mad.fablab.rest.server.configuration.PushServiceConfiguration;
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
    private PushServiceConfiguration mPushServiceConfiguration;

    public PushFacade(PushServiceConfiguration aPushServiceConfiguration,SessionFactory aSessionFactory){
        mSessionFactory = aSessionFactory;
        mPushServiceConfiguration = aPushServiceConfiguration;
    }

    public void pushToAllDevices(String aMessage){
        PushContent content = new PushContent();
        RegistrationIdFacade registrationIdFacade = new RegistrationIdFacade(new RegistrationIdDAO(mSessionFactory));
        List<RegistrationId> registrationIds = registrationIdFacade.findAll();
        for(RegistrationId registrationId : registrationIds){
            content.addRegId(registrationId.getRegistrationid());
        }
        content.createData("Hinweis",aMessage);
        AndroidPushService pushService = new AndroidPushService(mPushServiceConfiguration);
        pushJson(pushService,content);
    }

    public void pushToDevice(RegistrationId aRegistrationId, String aMessage){
        PushContent content = new PushContent();
        content.addRegId(aRegistrationId.getRegistrationid());
        content.createData("Hinweis",aMessage);
        AndroidPushService pushService = new AndroidPushService(mPushServiceConfiguration);
        pushJson(pushService,content);
    }

    private void pushJson(AndroidPushService aAndroidPushService, PushContent aPushContent){
        try{
            aAndroidPushService.pushJson(aPushContent);
        }catch (IOException io){
            io.printStackTrace();
        }

    }
}
