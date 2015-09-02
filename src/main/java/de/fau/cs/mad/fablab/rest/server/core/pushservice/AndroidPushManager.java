package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.fau.cs.mad.fablab.rest.core.PushToken;
import de.fau.cs.mad.fablab.rest.server.configuration.AndroidPushConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidPushManager implements PushManger {

    private AndroidPushService androidPushService;

    public AndroidPushManager(AndroidPushConfiguration configuration){
        this.androidPushService = new AndroidPushService(configuration);
    }

    @Override
    public void sendNotificationDoorJustOpened(List<PushToken> tokens, DoorState doorState) {
        for(PushToken token : tokens){
            System.out.println("ANDROID PUSH TO: " + token);
        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("doorstate", doorState);

        pushObject(tokens, objectMap);
    }

    @Override
    public void sendCartStautsChanged(PushToken token, CartStatus status) {
        if(token.getToken().length() > 0)
            System.out.println("ANDROID PUSH FOR CARTSTATUS: " + token + " STATUS: " + status.toString());

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("cartstatus", status);

        pushObject(token, objectMap);
    }

    /**
     * push a map of objects to a client device
     * @param token Target device Token (receiver device)
     * @param objects Map of objects to push to device
     * @see AndroidPushManager#pushObject(List, Map)
     */
    public void pushObject(PushToken token, Map<String, Object> objects) {
        List<PushToken> tokenList = new ArrayList<>();
        tokenList.add(token);

        pushObject(tokenList, objects);
    }

    /**
     * Push a map of objects to a list of client devices
     * @param targets List of targets devices
     * @param objects Map of objects to push to device
     */
    public void pushObject(List<PushToken> targets, Map<String, Object> objects) {
        AndroidPushContent pushContent = new AndroidPushContent();

        for (PushToken target : targets) {
            pushContent.addRegId(target.getToken());
        }
        for (String key : objects.keySet()) {
            pushContent.addData(key, objects.get(key));
        }

        try {
            androidPushService.pushJson(pushContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
