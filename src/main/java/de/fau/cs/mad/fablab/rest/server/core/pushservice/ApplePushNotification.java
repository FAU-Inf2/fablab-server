package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.server.configuration.APNConfiguration;

import java.util.Random;

public class ApplePushNotification {

    public ApplePushNotification(APNConfiguration configuration){
        Random random = new Random();
        String testMessage = "Hello Test! " + random.nextInt(1000);
        String testToken = "e6878d3993abfaec48220b9d4d3ea0b576c22351c7fbbb5faeb5449bf7f24452";
        //e6878d39 93abfaec 48220b9d 4d3ea0b5 76c22351 c7fbbb5f aeb5449b f7f24452

        ApplePushService notifier=null;
        try {
            notifier = new ApplePushService(configuration);
            notifier.sendpush(testMessage, testToken);
        } catch(Exception ex) {
            System.out.println(ex);
        } finally {
            if (notifier!=null)
                notifier.closeSender();
        }
    }

}
