package de.fau.cs.mad.fablab.rest.server.managed;

import de.fau.cs.mad.fablab.rest.server.configuration.AdminConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.NetworkConfiguration;
import io.dropwizard.lifecycle.Managed;
import it.sauronsoftware.cron4j.Scheduler;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateDatabaseManager implements Managed {

    final Scheduler scheduler;
    final String username;
    final String password;
    int port = -1;
    String hostname;

    URL url;
    CloseableHttpClient httpClient;
    HttpClientContext httpClientContext;

    public UpdateDatabaseManager(AdminConfiguration adminConfiguration, NetworkConfiguration networkConfiguration) {

        this.username = adminConfiguration.getUsername();
        this.password = adminConfiguration.getPassword();
        this.hostname = networkConfiguration.getHostname();
        this.port = networkConfiguration.getAdminPort();
        httpClient = HttpClients.custom().build();

        Credentials adminCredentials = new UsernamePasswordCredentials(username, password);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, adminCredentials);

        httpClientContext = HttpClientContext.create();
        httpClientContext.setCredentialsProvider(credentialsProvider);

        // Creates a Scheduler instance.
        scheduler = new Scheduler();
        // Schedule a once-a-minute task.
        scheduler.schedule("0 0 * * *", new Runnable() {
            public void run(){
                HttpPost request = new HttpPost(url.toString());
                try {
                    HttpResponse response = httpClient.execute(request, httpClientContext);
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void start() throws MalformedURLException {
        if(port != -1){
            url = new URL("https://"+hostname+":"+port+"/tasks/UpdateProductDatabaseTask");
            scheduler.start();
        }
    }

    @Override
    public void stop() throws Exception {
        scheduler.stop();
    }
}
