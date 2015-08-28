package de.fau.cs.mad.fablab.rest.server.core.pushservice;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import de.fau.cs.mad.fablab.rest.core.CartStatus;
import de.fau.cs.mad.fablab.rest.core.TriggerPushType;
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
                applePushService.sendpush(message, token, TriggerPushType.DOOR_OPENS_NEXT_TIME);
            } catch (InterruptedException | MalformedTokenStringException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendCartStautsChanged(String token, CartStatus status) {
        if(token.length() > 0) {
            String message = getCartChangedText(status);
            System.out.println("APPLE PUSH FOR CARTSTATUS: " + token + " Message: " + message);
            try {
                applePushService.sendpush(message, token, TriggerPushType.CART_STATUS_CHANGED);
            } catch (InterruptedException | MalformedTokenStringException e) {
                e.printStackTrace();
            }
        }
    }


    //Close / shutdown
    public void shutDown(){
        if(applePushService != null)
            applePushService.closeSender();
    }


    private String getCartChangedText(CartStatus status){
        switch (status){
            case PAID: return "Warenkorb wurde erfolgreich bezahlt";
            case CANCELLED: return "Bezahlvorgang wurde abgebrochen";
            case FAILED: return "Bezahlvorgang ist Fehlgeschlagen";
            default: return "";
        }
    }
}
