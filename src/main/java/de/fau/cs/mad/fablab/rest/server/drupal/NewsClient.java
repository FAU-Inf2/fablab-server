package de.fau.cs.mad.fablab.rest.server.drupal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewsClient implements NewsInterface {

    private static NewsInterface instance;
    private static NewsConfiguration config = null;

    private String baseUrl;
    private String allNewsUrl;
    private String fabUrl;

    private static final String JSON = ".json";

    private static final String LOGO = "/sites/fablab.fau.de/files/fablab_logo.png";

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
        allNewsUrl = baseUrl + JSON + "/?parameters[type]=story&page=";
        fabUrl = config.getFaburl();
    }

    public static void setConfiguration(NewsConfiguration c) {
        config = c;
    }

    /***
     * Gets the news-node from the Drupal with 'id' and parses it into a {@link News}-Object
     *
     * @param id the news id
     * @return a {@link News}-Object
     */
    @Override
    public News findById(long id) {
        String url = baseUrl + "/" + id + JSON;
        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        Response jsonResponse = client.target(url).request(MediaType.APPLICATION_JSON).get();
        if (jsonResponse.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + url);
        }

        DrupalNode node = jsonResponse.readEntity(DrupalNode.class);
        return getNewsFromNode(node);
    }

    /***
     * Gets news-nodes from the Drupal and parses them into a List of {@link News}
     *
     * @param offset the record offset
     * @param limit  the maximum number of products to return
     * @return a List of {@link News}
     */
    @Override
    public List<News> find(int offset, int limit) {
        List<News> allNews = new LinkedList<>();

        // news per page = 20!
        int pageSize = 20;
        int maxNews = limit - offset;

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        String url = "";
        Response jsonResponse = null;
        DrupalNodeShort[] nodes = null;

        int page = (offset / pageSize);
        int start = offset % pageSize;
        do {
            url = allNewsUrl + page;
            jsonResponse = client.target(url).request(MediaType.APPLICATION_JSON).get();

            if (jsonResponse.getStatus() != 200) {
                throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + url);
            }

            nodes = jsonResponse.readEntity(DrupalNodeShort[].class);

            for (int i = start; i < nodes.length; i++) {
                allNews.add(findById(nodes[i].getNid()));

                if (allNews.size() == maxNews) return allNews;
            }

            start = 0;
            page++;
        } while (nodes.length > 0);

        return allNews;
    }

    /***
     * Gets all news-nodes from the Drupal and parses them into a List of {@link News}
     *
     * @return a List of {@link News}
     */
    @Override
    public List<News> findAll() {
        List<News> allNews = new LinkedList<>();

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        String url = "";
        Response jsonResponse = null;
        DrupalNodeShort[] nodes = null;
        int page = 0;
        do {
            url = allNewsUrl + page;
            jsonResponse = client.target(url).request(MediaType.APPLICATION_JSON).get();

            if (jsonResponse.getStatus() != 200) {
                throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + url);
            }

            nodes = jsonResponse.readEntity(DrupalNodeShort[].class);

            for (int i = 0; i < nodes.length; i++) {
                allNews.add(findById(nodes[i].getNid()));
            }

            page++;
        } while (nodes.length > 0);

        return allNews;
    }

    /***
     * Parses a {@link DrupalNode} into a {@link News}-object
     *
     * @param node a {@link DrupalNode} to be parsed
     * @return a {@link News}-object
     */
    private News getNewsFromNode(DrupalNode node) {
        String body = node.getBody();
        News news = new News();
        news.setTitle(node.getTitle());
        news.setDescription(body);
        news.setDescriptionShort(node.getTeaser());
        news.setLink(fabUrl + "/" + node.getPath());
        news.setCreator(node.getName());
        news.setPubDate(new Date(node.getCreated() * 1000));
        news.setLinkToPreviewImage(fabUrl + extractImageLink(body));
        return news;
    }

    /***
     * Extracts the first image of a given 'body'-String and returns it
     *
     * @param body a {@link DrupalNode} to be parsed
     * @return the link to the image, if no image is found return link to FabLab-Logo
     */
    private String extractImageLink(String body) {
        // not very effictive (?)..
        String beforeLink = "<img alt=\"\" class=\"lightbox\" src=\"";

        String link = "";

        int index = body.indexOf(beforeLink);
        if (index == -1) {
            // no image-link found, return fablab-logo
            return LOGO;
        }

        int i = index + beforeLink.length();
        char c = '\0';
        while ((c = body.charAt(i)) != '\"') {
            link += c;
            i++;
        }
        return link;
    }
}
