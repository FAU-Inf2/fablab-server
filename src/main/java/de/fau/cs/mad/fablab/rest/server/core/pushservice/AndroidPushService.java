package de.fau.cs.mad.fablab.rest.server.core.pushservice;



import com.fasterxml.jackson.databind.ObjectMapper;
import de.fau.cs.mad.fablab.rest.server.configuration.AndroidPushConfiguration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AndroidPushService {

    private static final String REGISTRATION_ID = "";
    private static final String API_KEY = "";

    private AndroidPushConfiguration mPushServiceConfiguration;

    public AndroidPushService(AndroidPushConfiguration aPushServiceConfiguration){
        mPushServiceConfiguration = aPushServiceConfiguration;
    }

    public void pushJson(AndroidPushContent aPushContent) throws IOException{
        URL url = new URL(mPushServiceConfiguration.getGooglePushServiceURL());
        HttpsURLConnection
                .setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setUseCaches(false);
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("Authorization", "key=" + mPushServiceConfiguration.getPushAPIRegistrationId());

        ObjectMapper mapper = new ObjectMapper();
        DataOutputStream wr = new DataOutputStream(httpsURLConnection.getOutputStream());
        mapper.writeValue(wr, aPushContent);

        wr.flush();
        wr.close();

        int responseCode = httpsURLConnection.getResponseCode();
        System.out.println("Responsecode: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                httpsURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());

    }

    private static class CustomizedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
