package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.fau.cs.mad.fablab.rest.core.PushToken;

import java.util.List;

public interface PushManger {

    void sendNotificationDoorJustOpened(List<PushToken> tokens, DoorState doorState);
    void sendCartStautsChanged(PushToken token, CartStatus status);
}
