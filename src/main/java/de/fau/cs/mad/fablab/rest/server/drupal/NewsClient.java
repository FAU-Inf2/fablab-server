package de.fau.cs.mad.fablab.rest.server.drupal;

import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;

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
        return null;
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
