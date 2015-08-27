package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.api.PushType;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.PushToken;

import java.util.HashMap;

public class PushFacade {

    //Singleton
    private static PushFacade instance;
    private PushFacade(){}
    public static PushFacade getInstance () {
        if (PushFacade.instance == null)
            PushFacade.instance = new PushFacade ();
        return PushFacade.instance;
    }

    private HashMap<PushType, PushManger> pushMangers = new HashMap<>();

    public void addPushManager(PushManger manger, PushType type){
        pushMangers.put(type, manger);
    }

    public void subscribeDoorOpensNextTime(PushToken token){

    }

    public void unsubscribeDoorOpensNextTime(PushToken token){

    }

    public void fablabDoorJustOpened(){


    }

    public void cartStatusChanged(PushToken token, CartStatus state){


    }


}
