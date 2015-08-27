package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.PushToken;
import de.fau.cs.mad.fablab.rest.core.TriggerPushType;

import java.util.HashMap;
import java.util.Map;

public class PushFacade {

    //Singleton
    private static PushFacade instance;
    private PushFacade(){}
    public static PushFacade getInstance () {
        if (PushFacade.instance == null)
            PushFacade.instance = new PushFacade ();
        return PushFacade.instance;
    }

    //DAO
    private PushDAO dao;
    public void setDao(PushDAO dao){
        this.dao = dao;
    }

    //PUSH MANAGER
    private HashMap<PlatformType, PushManger> pushMangers = new HashMap<>();
    public void addPushManager(PushManger manger, PlatformType type){
        pushMangers.put(type, manger);
    }

    //DOOR STATE (DOOR OPENED)
    public void subscribeDoorOpensNextTime(PushToken token){
        this.dao.subscribeDoorOpensNextTime(token);
    }

    public void unsubscribeDoorOpensNextTime(PushToken token){
        this.dao.unsubscribeDoorOpensNextTime(token);
    }

    public void fablabDoorJustOpened(){
        for(Map.Entry<PlatformType, PushManger> entry : pushMangers.entrySet())
            entry.getValue().sendNotificationDoorJustOpened(this.dao.findAllTokensWith(entry.getKey(), TriggerPushType.DOOR_OPENS_NEXT_TIME));
    }


    //CART STATUS CHANGED
    public void cartStatusChanged(PushToken token, CartStatus state){


    }


}
