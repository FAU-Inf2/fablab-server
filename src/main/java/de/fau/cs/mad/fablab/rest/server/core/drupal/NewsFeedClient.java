package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    private String fabUrl;
    private String feedUrl;

    private List<News> allNews;

    private Date lastUpdate;
    //private final long TIMESPAN = 3600000L; // 1h
    private final long TIMESPAN = 600000L; // 10 min

    private static final Object LOCK = new Object();

    private static final String LOGO = "/sites/fablab.fau.de/files/fablab_logo.png";

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
        fabUrl = config.getFaburl();
        feedUrl = config.getFeedurl();
    }

    public static void setConfiguration(NewsConfiguration c) {
        config = c;
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
        if ((now.getTime() - lastUpdate.getTime()) > TIMESPAN) return true;
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

        List<News> allNews = null;
        try {
            allNews = parseNews(feed.getChannel().getItem());
        } catch (ParseException e) {
            System.err.println("ERROR - ParseException while parsing News. \n" +
                    "The Reason is : " + e.getMessage());
        }

        synchronized(LOCK) {
            if (allNews != null) {
                this.allNews = allNews;
                this.lastUpdate = new Date();
            }
        }
    }

    private List<News> parseNews(List<RSSFeedItem> items) throws ParseException {
        List<News> news = new LinkedList<>();
        for (RSSFeedItem item : items) {
            news.add(getNewsFromRSSFeedItem(item));
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
        String imageLink = extractImageLink(doc);

        News news = new News();
        news.setId(Long.parseLong(extractId(item.getGuid())));
        news.setTitle(item.getTitle());
        news.setDescription(parseBody(doc));
        news.setDescriptionShort(removeHTML(doc));
        news.setIsPermaLink(false);
        news.setLink(item.getLink());
        news.setCreator(item.getCreator());
        news.setPubDate(dateFormat.parse(item.getPubDate()));
        news.setLinkToPreviewImage(imageLink);
        news.setCategory(item.getCategory());
        return news;
    }

    /***
     * Extracts the first image of a given {@link Document} and returns it
     *
     * @param doc the input {@link Document}
     * @return the link to the image, if no image is found return link to FabLab-Logo
     */
    private String extractImageLink(Document doc) {
        Element image = doc.select("img").first();

        // no image found, return null
        if (image == null) return null;

        return image.attr("abs:src");
    }

    private String extractId(String guid) {
        return guid.substring(0, guid.indexOf(' '));
    }

    /***
     * Removes the first image, fixes relative links and replaces <li> and </li>-tags
     *
     * @param doc the input document
     * @return the parsed body
     */
    private String parseBody(Document doc) {
        doc = removeFirstImg(doc);
        doc = fixElements(doc);
        return doc.body().toString();
    }

    /***
     * Removes the first image
     *
     * @param doc the input Document
     * @return the parsed body
     */
    private Document removeFirstImg(Document doc) {
        Element image = doc.select("img").first();

        if (image != null) {
            // remove first image and link
            image.remove();
            //doc.select("a").first().remove();
            String link = image.attr("abs:src");

            for (Element a : doc.select("a")) {
                if (link.equals(a.attr("abs:href"))) {
                    a.remove();
                    break;
                }
            }
        }

        return doc;
    }

    /***
     * Fixes relative-links and relative-links to images and adds "- " at the beginning and "<br>" at the end
     * of <li></li>-tags
     *
     * @param doc the input text
     * @return the parsed text
     */
    private Document fixElements(Document doc) {
        for (Element a : doc.select("a")) {
            String link = a.attr("abs:href");
            a.attr("href", link);
        }

        for (Element img : doc.select("img")) {
            String link = img.attr("abs:src");
            img.attr("src", link);
        }

        for (Element li : doc.select("li")) {
            li.prepend("- ");
            li.append("<br />");
        }

        return doc;
    }

    /***
     * Removes all HTML-Tags in the given Document
     *
     * @param doc the input text
     * @return the parsed text
     */
    private String removeHTML(Document doc) {
        return doc.body().text();
    }

    /***
     * Removes all \n and nbsp; from the given text
     *
     * @param text the input text
     * @return the parsed text
     */
    private String removeNewlines(String text) {
        text = text.replaceAll("&nbsp;", "");
        return text.replaceAll("\n", "");
    }
}
