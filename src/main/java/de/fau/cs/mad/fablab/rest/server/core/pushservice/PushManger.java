package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.CartStatus;

import java.util.List;

public interface PushManger {

    void sendNotificationDoorJustOpened(List<String> tokens);
    void sendCartStautsChanged(String token, CartStatus status);
}
