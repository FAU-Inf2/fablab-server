package de.fau.cs.mad.fablab.rest.server.core.pushservice;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.server.configuration.ApplePushConfiguration;


public class ApplePushManager implements PushManger{


    private ApplePushService applePushService;

    public ApplePushManager(ApplePushConfiguration configuration){
        try {
            this.applePushService = new ApplePushService(configuration);
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }



    public void sendNotificationDoorJustOpens(String[] tokens){
        //TODO -> Send Notification to all
//        token = "fef9ae99842c1714c6749dc4106c3ceb61b9f08c388ef8a2ad261d1acdcf4526";
//        String message = "Das Fablab ist hat gerade geöffnet";
//        try {
//            applePushService.sendpush(message, token);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (MalformedTokenStringException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void sendCartStautsChanged(String token, CartStatus status) {

    }


    //Use push Notifications
    public void nofityfiMeOnNextTimeDoorOpens(String token){

    }


    //Close / shutdown
    public void shutDown(){
        if(applePushService != null)
            applePushService.closeSender();
    }

}
