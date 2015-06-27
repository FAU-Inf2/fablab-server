package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class NewsFeedClient implements NewsInterface {

    private static NewsInterface instance;
    private static NewsConfiguration config = null;

    private String fabUrl;
    private String feedUrl;

    private List<News> allNews;

    private static final String LOGO = "/sites/fablab.fau.de/files/fablab_logo.png";

    //Tue, 12 May 2015 11:29:28 +0000
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

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
        if (allNews == null) updateNews();
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
        updateNews();

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
        updateNews();
        return allNews;
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

        if (allNews != null) this.allNews = allNews;
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
        String imageLink = extractImageLink(body);

        News news = new News();
        news.setId(Long.parseLong(extractId(item.getGuid())));
        news.setTitle(item.getTitle());
        news.setDescription(parseBody(body));
        news.setDescriptionShort(removeHTML(body));
        news.setIsPermaLink(false);
        news.setLink(item.getLink());
        news.setCreator(item.getCreator());
        //news.setPubDate(dateFormat.parse(item.getPubDate()));
        news.setPubDate(new Date(item.getPubDate())); // TODO
        news.setLinkToPreviewImage(imageLink);
        news.setCategory(item.getCategory());
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

        // no image found, return null
        if (parts.length == 1) return null;//return fabUrl + LOGO;

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

    private String extractId(String guid) {
        return guid.substring(0, guid.indexOf(' '));
    }

    /***
     * Removes the first image, fixes relative links and replaces <li> and </li>-tags
     *
     * @param body the input body
     * @return the parsed body
     */
    private String parseBody(String body) {
        body = removeFirstImg(body);
        body = fixLinks(body);
        body = fixListElements(body);
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
     * Replaces <li>-tags with "- " and </li>-tags with newline
     *
     * @param body the input body
     * @return the parsed body
     */
    private String fixListElements(String body) {
        body = body.replaceAll("<li>", "- ");
        return body.replaceAll("</li>", "<br />");
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
