package de.fau.cs.mad.fablab.rest.server.core.pushservice;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.server.configuration.ApplePushConfiguration;

import java.util.List;


public class ApplePushManager implements PushManger{


    private ApplePushService applePushService;

    public ApplePushManager(ApplePushConfiguration configuration){
        try {
            this.applePushService = new ApplePushService(configuration);
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }



    public void sendNotificationDoorJustOpened(List<String> tokens){
        String message = "Das Fablab hat gerade geöffnet";
        for(String token : tokens){
            try {
                applePushService.sendpush(message, token);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedTokenStringException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendCartStautsChanged(String token, CartStatus status) {
        if(token.length() > 0) {
            String message = "Warenkorb Status: " + status.toString();
            System.out.println("APPLE PUSH FOR CARTSTATUS: " + token + " Message: " + message);
            try {
                applePushService.sendpush(message, token);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedTokenStringException e) {
                e.printStackTrace();
            }
        }
    }


    //Close / shutdown
    public void shutDown(){
        if(applePushService != null)
            applePushService.closeSender();
    }

}
