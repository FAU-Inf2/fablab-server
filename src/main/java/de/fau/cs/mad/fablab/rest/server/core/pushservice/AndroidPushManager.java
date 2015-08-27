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

/**
    Alter code von der Fasade -> Alles Android spezifische sollte jetzt im Manager getan werden um die Trennung zu wahren!

 */
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
