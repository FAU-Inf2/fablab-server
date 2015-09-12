package de.fau.cs.mad.fablab.rest.server.core.drupal;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.server.configuration.GeneralDataConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DrupalClient implements DrupalInterface {

    private static DrupalInterface instance;
    private static NewsConfiguration config = null;
    private static GeneralDataConfiguration dataConfig = null;

    private String baseUrl;
    private String toolsUrl;
    private String fabUrl;

    private static final String JSON = ".json";

    private LinkedList<FabTool> allTools;
    private Date lastUpdate;
    private final long TIMESPAN = 86400000L; // 1 day

    private static final FabTool SONSTIGES = new FabTool("Sonstiges", null, null, null, null);

    /***
     * Singleton getInstance()
     * @return instance
     */
    public static DrupalInterface getInstance() {
        if (instance == null) {
            instance = new DrupalClient();
        }
        return instance;
    }

    /***
     * Checks for valid configuration and sets necessary urls
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     */
    private DrupalClient() {
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing DrupalClient. Configuration vars missing.\n" +
                    "The configuration (url, port and endpoint) has to be set \n " +
                    "using the class NewsConfiguration.\n");
            System.exit(1);
        }
        
        fabUrl = dataConfig.getFabUrl();
        toolsUrl = fabUrl + "/tool";
    }

    public static void setConfiguration(NewsConfiguration c, GeneralDataConfiguration dc) {
        config = c;
        dataConfig = dc;
    }

    /***
     * Gets all tool-nodes from the Drupal
     *
     * @return a List of {@link FabTool}-Objects
     */
    @Override
    public List<FabTool> findAllTools() {
        if (updateNeeded()) updateToolList();
        return allTools;
    }

    /***
     * Gets the tool-node from the Drupal with 'id' and parses it into a {@link FabTool}-Object
     *
     * @param id the tool id
     * @return a {@link FabTool}-Object
     */
    @Override
    public FabTool findToolById(long id) {
        for (FabTool tool : allTools) {
            if (tool.getId() == id) return tool;
        }
        return null;
    }

    /***
     * Checks if a update is needed (if ((now.getTime() - lastUpdate.getTime()) > TIMESPAN) )
     *
     * @return true, if update needed; otherwise false
     */
    public boolean updateNeeded() {
        if (allTools == null) return true;

        Date now = new Date();
        if ((now.getTime() - lastUpdate.getTime()) > TIMESPAN) return true;
        return false;
    }

    /***
     * Updates the tool-List
     */
    private void updateToolList() {
        LinkedList<FabTool> allTools = new LinkedList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(toolsUrl).get();
            doc.setBaseUri(fabUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements toolList = doc.select("ul[class=view-tools-list]").select("li");

        int counter = 0;
        for (Element li: toolList) {
            FabTool tool = getToolFromLiElement(li);
            if (tool != null) {
                tool.setId(counter++);
                allTools.add(tool);
            }
        }

        if (allTools != null) {
            // sort tools
            Collections.sort(allTools, new ToolsComparator());
            // add "Sonstiges"
            SONSTIGES.setId(counter);
            allTools.addFirst(SONSTIGES);

            this.allTools = allTools;
            this.lastUpdate = new Date();
        }
    }

    private FabTool getToolFromLiElement(Element li) {
        Element a = li.select("h2[class=title]").select("a").first();
        Element content = li.select("div[class=\"content clearfix\"]").first();

        if (a == null || content == null) return null;

        FabTool tool = new FabTool();
        tool.setTitle(a.text());
        tool.setDescription(content.text());
        tool.setDetails(content.toString());
        tool.setLink(a.attr("abs:href"));
        tool.setLinkToPicture(getImageLink(content));
        return tool;
    }

    private String getImageLink(Element content) {
        Element image = content.select("img").first();
        if (image != null) return image.attr("abs:src");
        else return null;
    }
}
