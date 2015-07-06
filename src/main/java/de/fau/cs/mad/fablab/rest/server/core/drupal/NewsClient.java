package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

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
     * @param limit  the maximum number of News to return
     * @return a List of {@link News}
     */
    @Override
    public List<News> find(int offset, int limit) {
        List<News> allNews;// = new LinkedList<>();
        LinkedHashMap<Integer, News> news = new LinkedHashMap<>();

        // news per page = 20!
        int pageSize = 20;

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        String url = "";
        Response jsonResponse = null;
        DrupalNodeShort[] nodes = null;

        int size = 0;
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
                //allNews.add(findById(nodes[i].getNid()));
                int nid = nodes[i].getNid();
                if (!news.containsKey(nid)) {
                    news.put(nid, findById(nid));
                    size++;
                }

                if (size == limit) break;
            }
            if (size == limit) break;

            start = 0;
            page++;
        } while (nodes.length > 0);

        allNews = new LinkedList<>(news.values());
        return allNews;
    }

    /***
     * Gets all news-nodes from the Drupal and parses them into a List of {@link News}
     *
     * @return a List of {@link News}
     */
    @Override
    public List<News> findAll() {
        List<News> allNews;// = new LinkedList<>();
        LinkedHashMap<Integer, News> news = new LinkedHashMap<>();

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

            for (DrupalNodeShort node : nodes) {
                int nid = node.getNid();
                if (!news.containsKey(nid)) news.put(nid, findById(nid));
            }

            page++;
        } while (nodes.length > 0);

        allNews = new LinkedList<>(news.values());
        return allNews;
    }

    @Override
    public long lastUpdate() {
        return -1;
    }

    /***
     * Parses a {@link DrupalNode} into a {@link News}-object
     *
     * @param node a {@link DrupalNode} to be parsed
     * @return a {@link News}-object
     */
    private News getNewsFromNode(DrupalNode node) {
        String body = node.getBody();
        String imageLink = extractImageLink(body);

        News news = new News();
        news.setId(node.getNid());
        news.setTitle(node.getTitle());
        news.setDescription(parseBody(body));
        news.setDescriptionShort(removeHTML(node.getTeaser()));

        // check for missing permalink
        String link = node.getPath();
        if (link == null) {
            news.setIsPermaLink(false);
            link = "node/" + node.getNid();
        } else {
            news.setIsPermaLink(true);
        }
        news.setLink(fabUrl + "/" + link);

        news.setCreator(node.getName());
        news.setPubDate(new Date(node.getCreated() * 1000));
        news.setLinkToPreviewImage(imageLink);
        return news;
    }

    /***
     * Extracts the first image of a given 'body'-String and returns it
     *
     * @param body the input body
     * @return the link to the image, if no image is found return link to FabLab-Logo
     */
    private String extractImageLink(String body) {
        String[] parts = body.split("<img.*?src=.*?\"", 2);

        // no image found, return FabLab-Logo
        if (parts.length == 1) return fabUrl + LOGO;

        String link = "";
        int i = 0;
        char c = '\0';
        while ((c = parts[1].charAt(i)) != '\"') {
            // if relative link, insert fabUrl
            if (i == 0 && c == '/') link += fabUrl;
            link += c;
            i++;
        }

        return link;
    }

    /***
     * Removes the first image and fixes relative links
     *
     * @param body the input body
     * @return the parsed body
     */
    private String parseBody(String body) {
        body = removeFirstImg(body);
        body = fixLinks(body);
        return body;
    }

    /***
     * Removes the first image
     *
     * @param body the input body
     * @return the parsed body
     */
    private String removeFirstImg(String body) {
        body = body.replaceFirst("<img.*?>", "");
        return body.replaceFirst("<a.*?></a>", "");
    }

    /***
     * Splits the given body at img- or a href-tags and calls fixLinksHelper(String[] parts, String tag)
     *
     * @param body the input body
     * @return the parsed body
     */
    private String fixLinks(String body) {
        String[] parts = body.split("<a href=.*?");
        String result = fixLinksHelper(parts, "<a href=\"");

        parts = result.split("<img.*?src=.*?");
        result = fixLinksHelper(parts, "<img alt=\"\" src=\"");

        return result;
    }

    /***
     * Reinserts the removed html-Tag and inserts the 'fabUrl' if a 'parts'-element contains a relative link
     *
     * @param parts the splitted input body
     * @param tag the tag to be reinserted (example: "<a href=\"" or "<img alt=\"\" src=\"")
     * @return the parsed body
     */
    private String fixLinksHelper(String[] parts, String tag) {
        String result = "";

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replaceFirst("internal:", fabUrl + "/");
            if (i > 0 && parts[i].charAt(0) == '"') {
                parts[i] = parts[i].replaceFirst("\"", "");
                result += tag;
                if (parts[i].charAt(0) == '/') result += fabUrl;
            }
            result += parts[i];
        }

        return result;
    }

    /***
     * Removes all HTML-Tags in the given text
     *
     * @param text the input text
     * @return the parsed text
     */
    private String removeHTML(String text) {
        return text.replaceAll("<.*?>", "");
    }
}
