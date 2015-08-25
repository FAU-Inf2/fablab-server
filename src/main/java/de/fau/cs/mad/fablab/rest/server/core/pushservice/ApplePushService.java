package de.fau.cs.mad.fablab.rest.server.core.pushservice;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.FailedConnectionListener;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import de.fau.cs.mad.fablab.rest.server.configuration.APNConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLHandshakeException;


public class ApplePushService{

    private final PushManager<SimpleApnsPushNotification> pushManager;
    private final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
    private APNConfiguration configuration;


    public ApplePushService(APNConfiguration configuration) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException, IOException {
        this.configuration = configuration;

        Path keyFile = Paths.get(configuration.getCertificate());
        URI cert = keyFile.toUri();
        this.pushManager = new PushManager<>(
                ApnsEnvironment.getSandboxEnvironment(),
                SSLContextUtil.createDefaultSSLContext(cert.getPath(),configuration.getPassword()),
                null, // Optional: custom event loop group
                null, // Optional: custom ExecutorService for calling listeners
                null, // Optional: custom BlockingQueue implementation
                new PushManagerConfiguration(),
                "ExamplePushManager");
        pushManager.start();
        pushManager.registerRejectedNotificationListener(new MyRejectedNotificationListener());
        pushManager.registerFailedConnectionListener(new MyFailedConnectionListener());
    }


    public void sendpush(String message, String stoken) throws InterruptedException, MalformedTokenStringException {
        byte[]token = TokenUtil.tokenStringToByteArray(stoken);
        String payload = payloadBuilder.setAlertBody(message).setSoundFileName("ring-ring.aiff").buildWithDefaultMaximumLength();
        SimpleApnsPushNotification notification = new SimpleApnsPushNotification(token, payload);
        pushManager.getQueue().put(notification);
        System.out.println("Queued: "+notification);
    }

    public void closeSender() {
        if(pushManager!=null) {
            try {
                pushManager.shutdown();
            } catch (InterruptedException ex) {
                Logger.getLogger(ApplePushService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class MyRejectedNotificationListener implements RejectedNotificationListener<SimpleApnsPushNotification> {
        @Override
        public void handleRejectedNotification(
                final PushManager<? extends SimpleApnsPushNotification> pushManager,
                final SimpleApnsPushNotification notification,
                final RejectedNotificationReason reason) {
            System.out.format("%s was rejected with rejection reason %s\n", notification, reason);
        }
    }

    private class MyFailedConnectionListener implements FailedConnectionListener<SimpleApnsPushNotification> {
        @Override
        public void handleFailedConnection(
                final PushManager<? extends SimpleApnsPushNotification> pushManager,
                final Throwable cause) {
            if (cause instanceof SSLHandshakeException)
                System.out.println("This is probably a permanent failure, and we should shut down the PushManager.");
            else
                System.out.println(cause);
        }

    }
}