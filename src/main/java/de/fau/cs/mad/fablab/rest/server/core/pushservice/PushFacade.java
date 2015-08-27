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
//    public void pushToAllDevices(String aTitel,Object aMessage){
//        AndroidPushContent content = new AndroidPushContent();
//        PushTokenFacade registrationIdFacade = new PushTokenFacade(new PushTokenDAO(mSessionFactory));
//        List<PushToken> pushTokens = registrationIdFacade.findAll();
//        for(PushToken pushToken : pushTokens){
//            content.addRegId(pushToken.getToken());
//        }
//        content.addData(aTitel, aMessage);
//        AndroidPushService pushService = new AndroidPushService(mPushServiceConfiguration);
//        pushJson(pushService,content);
  //  }

  //  public void pushToDevice(PushToken aPushToken, String aTitel,String aMessage){
//        AndroidPushContent content = new AndroidPushContent();
//        content.addRegId(aPushToken.getToken());
//        content.addData(aTitel,aMessage);
//        AndroidPushService pushService = new AndroidPushService(mPushServiceConfiguration);
//        pushJson(pushService,content);
 //   }

//    private void pushJson(AndroidPushService aAndroidPushService, AndroidPushContent aPushContent){
//        try{
//            aAndroidPushService.pushJson(aPushContent);
//        }catch (IOException io){
//            io.printStackTrace();
//        }
//    }


}
