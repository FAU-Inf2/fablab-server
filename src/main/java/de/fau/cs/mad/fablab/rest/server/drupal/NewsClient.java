package de.fau.cs.mad.fablab.rest.server.drupal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class NewsClient implements NewsInterface {

    private static NewsInterface instance;
    private static NewsConfiguration config = null;

    private String baseUrl;
    private String newsUrl;
    private String newsIdUrl;
    private static final String PAGE = "&page=";

    /***
     * Singleton getInstance()
     * @return instance
     */
    public static NewsInterface getInstance() {
        if (instance == null) {
            instance = new NewsClient();
        }
        return instance;
    }

    /***
     * Checks for valid configuration and sets necessary urls
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     */
    private NewsClient() {
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing NewsClient. Configuration vars missing.\n" +
                    "The configuration (url, port and endpoint) has to be set \n " +
                    "using the class NewsConfiguration.\n");
            System.exit(1);
        }

        baseUrl = config.getUrl() + ":" + config.getPort() + "/" + config.getNodeEndpoint();
        newsUrl = baseUrl + "/?parameters[type]=story";
        newsIdUrl = baseUrl + "/?parameters[nid]=";
    }

    public static void setConfiguration(NewsConfiguration c) {
        config = c;
    }

    @Override
    public News findById(long id) {
        // newsIdUrl += id
        String url = newsIdUrl+id;
        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        Response jsonResponse = client.target(url).request(MediaType.APPLICATION_JSON).get();
        //Response jsonResponse = client.target("http://52.28.16.59:50080/rest/node/"+id).request(MediaType.APPLICATION_XML).get();
        if (jsonResponse.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus());
        }

        DrupalNode[] node = jsonResponse.readEntity(DrupalNode[].class);
        System.out.println("################################ NEWS #########################");
        System.out.println(node[0].toString());
        return new News();
    }

    @Override
    public List<News> find(int offset, int limit) {
        return null;
    }

    @Override
    public List<News> findAll() {
        // page 1 : String page1 = newsUrl + PAGE + "1"
        return null;
    }
}
