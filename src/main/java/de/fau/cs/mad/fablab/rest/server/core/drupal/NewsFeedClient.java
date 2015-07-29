package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.GeneralDataConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NewsFeedClient implements NewsInterface {

    private static NewsInterface instance;
    private static NewsConfiguration config = null;
    private static GeneralDataConfiguration dataConfig = null;

    private String fabUrl;
    private String feedUrl;

    private LinkedList<News> allNews;

    private Date lastTry;
    private Date lastUpdate;
    //private final long TIMESPAN = 3600000L; // 1h
    private final long TIMESPAN = 600000L; // 10 min

    private static final Object LOCK = new Object();

    //Tue, 12 May 2015 11:29:28 +0000
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    /***
     * Singleton getInstance()
     * @return instance
     */
    public static NewsInterface getInstance() {
        if (instance == null) {
            instance = new NewsFeedClient();
        }
        return instance;
    }

    /***
     * Checks for valid configuration and sets necessary urls
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     */
    private NewsFeedClient() {
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing NewsFeedClient. Configuration vars missing.\n" +
                    "The configuration (faburl and feedurl) has to be set \n " +
                    "using the class NewsConfiguration.\n");
            System.exit(1);
        }
        fabUrl = dataConfig.getFabUrl();
        feedUrl = config.getFeedurl();
    }

    public static void setConfiguration(NewsConfiguration c, GeneralDataConfiguration dc) {
        config = c;
        dataConfig = dc;
    }

    @Override
    public News findById(long id) {
        if (updateNeeded()) updateNews();
        for (News news : allNews) {
            if (news.getId() == id) return news;
        }
        return null;
    }

    /***
     * Gets News from the RSS-Feed and parses them into a List of {@link News}
     *
     * @param offset the record offset
     * @param limit  the maximum number of News to return
     * @return a List of {@link News}
     */
    @Override
    public List<News> find(int offset, int limit) {
        if (updateNeeded()) updateNews();

        List<News> news = new LinkedList<>();

        ListIterator<News> iterator = null;
        try {
            iterator = this.allNews.listIterator(offset);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        int numElements = 0;
        while (iterator.hasNext()) {
            if (numElements == limit) break;
            news.add(iterator.next());
            numElements++;
        }
        return news;
    }

    /***
     * Gets all News from the RSS-Feed and parses them into a List of {@link News}
     *
     * @return a List of {@link News}
     */
    @Override
    public List<News> findAll() {
        if (updateNeeded()) updateNews();
        return allNews;
    }

    /***
     * Gets all News since 'timestamp' from the RSS-Feed and parses them into a List of {@link News}
     *
     * @param timestamp the timestamp
     * @return a List of {@link News}
     */
    @Override
    public List<News> findNewsSince(long timestamp) {
        if (updateNeeded()) updateNews();
        Date refDate = new Date(timestamp);
        List<News> newNews = new LinkedList<>();
        for (News n : allNews) {
            if (n.getPubDate().equals(refDate) || n.getPubDate().before(refDate)) break;
            else newNews.add(n);
        }
        return newNews;
    }

    /***
     *
     * @return timestamp of the last update
     */
    @Override
    public long lastUpdate() {
        if (lastUpdate != null) return lastUpdate.getTime();
        else return 0;
    }

    /***
     * Checks if a update is needed (if ((now.getTime() - lastUpdate.getTime()) > TIMESPAN) )
     *
     * @return true, if update needed; otherwise false
     */
    private boolean updateNeeded() {
        if (allNews == null) return true;

        Date now = new Date();
        if ((now.getTime() - lastTry.getTime()) > TIMESPAN) return true;
        return false;
    }

    /***
     * Retrieves the RSS-Feed from the given faburl + feedurl and updates the News-List
     *
     */
    private void updateNews() {
        URL url = null;

        try {
            url = new URL(fabUrl + feedUrl);
        } catch (MalformedURLException e) {
            System.err.println("ERROR - MalformedURLException while updating News. \n" +
                    "The Reason is : " + e.getMessage() + "\n" +
                    "Url was : " + fabUrl + feedUrl);
        }

        RSSFeed feed = null;

        try {
            InputStreamReader reader = new InputStreamReader(url.openStream());

            XmlMapper mapper = new XmlMapper();

            feed = mapper.readValue(reader, RSSFeed.class);

            reader.close();

        } catch (IOException e) {
            System.err.println("ERROR - IOException while updating News. \n" +
                    "The Reason is : " + e.getMessage());
        }

        if (this.allNews == null) {
            LinkedList<News> allNews = null;
            try {
                allNews = parseNews(feed.getChannel().getItem());
            } catch (ParseException e) {
                System.err.println("ERROR - ParseException while parsing News. \n" +
                        "The Reason is : " + e.getMessage());
            }

            synchronized (LOCK) {
                if (allNews != null) {
                    this.allNews = allNews;
                    this.lastTry = new Date();
                    this.lastUpdate = new Date();
                }
            }

        } else {

            LinkedList<News> newNews = null;
            try {
                newNews = parseNewNews(feed.getChannel().getItem());
            } catch (ParseException e) {
                System.err.println("ERROR - ParseException while parsing News. \n" +
                        "The Reason is : " + e.getMessage());
            }

            synchronized (LOCK) {
                if (newNews != null) {
                    News n;
                    while ((n = newNews.pollLast()) != null) {
                        this.allNews.addFirst(n);
                        this.lastUpdate = new Date();
                    }
                    this.lastTry = new Date();
                }
            }
        }
    }

    private LinkedList<News> parseNews(List<RSSFeedItem> items) throws ParseException {
        LinkedList<News> news = new LinkedList<>();
        for (RSSFeedItem item : items) {
            news.add(getNewsFromRSSFeedItem(item));
        }
        return news;
    }

    private LinkedList<News> parseNewNews(List<RSSFeedItem> items) throws ParseException {
        LinkedList<News> news = new LinkedList<>();
        for (RSSFeedItem item : items) {
            News n = getNewsFromRSSFeedItem(item);

            if (n.getId() == allNews.peekFirst().getId()) break;
            else news.add(n);
        }
        return news;
    }

    /***
     * Parses a {@link RSSFeedItem} into a {@link News}-object
     *
     * @param item a {@link RSSFeedItem} to be parsed
     * @return a {@link News}-object
     */
    private News getNewsFromRSSFeedItem(RSSFeedItem item) throws ParseException {
        String body = item.getDescription();
        Document doc = Jsoup.parse(body, fabUrl);
        String imageLink = HTMLHelper.extractImageLink(doc);

        News news = new News();
        news.setId(Long.parseLong(extractId(item.getGuid())));
        news.setTitle(item.getTitle());
        news.setDescription(HTMLHelper.parseBody(doc));
        news.setDescriptionShort(HTMLHelper.removeHTML(doc));
        news.setIsPermaLink(false);
        news.setLink(item.getLink());
        news.setCreator(item.getCreator());
        news.setPubDate(dateFormat.parse(item.getPubDate()));
        news.setLinkToPreviewImage(imageLink);
        news.setCategory(item.getCategory());
        return news;
    }

    private String extractId(String guid) {
        return guid.substring(0, guid.indexOf(' '));
    }

}
