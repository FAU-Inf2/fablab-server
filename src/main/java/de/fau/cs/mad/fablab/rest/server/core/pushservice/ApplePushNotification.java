package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import de.fau.cs.mad.fablab.rest.server.configuration.APNConfiguration;

public class ApplePushNotification {

    public ApplePushNotification(APNConfiguration configuration){
        String testMessage = "Hallo vom Fablab-Server ";
        String testToken = "fef9ae99842c1714c6749dc4106c3ceb61b9f08c388ef8a2ad261d1acdcf4526";

        ApplePushService notifier;
        try {
            notifier = new ApplePushService(configuration);
            notifier.sendpush(testMessage, testToken);
        } catch(Exception ex) {
            System.out.println(ex);
        } finally {
            //TODO -> Close @ Shutdown
            //if (notifier!=null)
               // notifier.closeSender();
        }
    }

}
