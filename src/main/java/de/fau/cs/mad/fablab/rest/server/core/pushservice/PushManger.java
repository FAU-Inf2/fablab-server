package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.CartStatus;

public interface PushManger {

    void sendNotificationDoorJustOpens(String[] tokens);
    void sendCartStautsChanged(String token, CartStatus status);
}
