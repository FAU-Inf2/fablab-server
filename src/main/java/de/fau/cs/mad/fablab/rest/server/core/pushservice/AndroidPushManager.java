package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.server.configuration.AndroidPushConfiguration;

public class AndroidPushManager implements PushManger {


    private AndroidPushService androidPushService;


    public AndroidPushManager(AndroidPushConfiguration configuration){
        this.androidPushService = new AndroidPushService(configuration);
    }

    @Override
    public void sendNotificationDoorJustOpens(String[] tokens) {

    }

    @Override
    public void sendCartStautsChanged(String token, CartStatus status) {

    }
}
