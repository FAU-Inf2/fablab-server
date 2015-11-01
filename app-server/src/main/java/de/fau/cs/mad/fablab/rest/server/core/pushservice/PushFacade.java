package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.core.*;

import java.util.HashMap;
import java.util.List;
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
    public Boolean subscribeDoorOpensNextTime(PushToken token){
        return this.dao.subscribeDoorOpensNextTime(token);
    }

    public Boolean unsubscribeDoorOpensNextTime(PushToken token){
        return this.dao.unsubscribeDoorOpensNextTime(token);
    }

    public Boolean doorOpensNextTimeIsSetForToken(PushToken token){
        return this.dao.doorOpensNextTimeIsSetForToken(token);
    }

    public void fablabDoorJustOpened(DoorState doorState){
        for(Map.Entry<PlatformType, PushManger> entry : pushMangers.entrySet()) {

            List<PushToken> tokenList = this.dao.findAllTokensWith(entry.getKey(), TriggerPushType.DOOR_OPENS_NEXT_TIME);
            if (!tokenList.isEmpty())
                entry.getValue().sendNotificationDoorJustOpened(tokenList, doorState);
        }

        this.dao.removeTokensForTrigger(TriggerPushType.DOOR_OPENS_NEXT_TIME);
    }


    //CART STATUS CHANGED
    public void cartStatusChanged(CartServer cartServer){
        PushToken token = new PushToken(cartServer.getPushToken());
        token.setPlatformType(cartServer.getPlatformType());

        PushManger pushManger = pushMangers.get(token.getPlatformType());
        pushManger.sendCartStautsChanged(token, cartServer.getStatus());
    }


}
