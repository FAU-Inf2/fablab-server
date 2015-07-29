package de.fau.cs.mad.fablab.rest.server.core.drupal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.server.configuration.NewsConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class DrupalClient implements DrupalInterface {

    private static DrupalInterface instance;
    private static NewsConfiguration config = null;

    private String baseUrl;
    private String toolsUrl;
    private String fabUrl;

    private static final String JSON = ".json";

    private LinkedList<FabTool> allTools;
    private Date lastUpdate;
    private final long TIMESPAN = 86400000L; // 1 day

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

        baseUrl = config.getUrl() + ":" + config.getPort() + "/" + config.getNodeEndpoint();
        toolsUrl = baseUrl + JSON + "/?parameters[type]=tool&page=";
        fabUrl = config.getFaburl();
    }

    public static void setConfiguration(NewsConfiguration c) {
        config = c;
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
    private boolean updateNeeded() {
        if (allTools == null) return true;

        Date now = new Date();
        if ((now.getTime() - lastUpdate.getTime()) > TIMESPAN) return true;
        return false;
    }

    /***
     * Updates the tool-List
     */
    private void updateToolList() {
        LinkedList<FabTool> allTools;
        LinkedHashMap<Integer, FabTool> tools = new LinkedHashMap<>();

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        String url = "";
        Response jsonResponse = null;
        DrupalNodeShort[] nodes = null;
        int page = 0;

        do {
            url = toolsUrl + page;
            jsonResponse = client.target(url).request(MediaType.APPLICATION_JSON).get();

            if (jsonResponse.getStatus() != 200) {
                throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + url);
            }

            nodes = jsonResponse.readEntity(DrupalNodeShort[].class);

            for (DrupalNodeShort node : nodes) {
                int nid = node.getNid();
                if (!tools.containsKey(nid)) tools.put(nid, getToolById(nid));
            }

            page++;
        } while (nodes.length > 0);

        allTools = new LinkedList<>(tools.values());

        if (allTools != null) {
            this.allTools = allTools;
            this.lastUpdate = new Date();
        }

    }

    /***
     * Gets the tool-node from the Drupal with 'id' and parses it into a {@link FabTool}-Object
     *
     * @param id the tool id
     * @return a {@link FabTool}-Object
     */
    private FabTool getToolById(long id) {
        String url = baseUrl + "/" + id + JSON;
        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        Response jsonResponse = client.target(url).request(MediaType.APPLICATION_JSON).get();
        if (jsonResponse.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + url);
        }

        DrupalNode node = jsonResponse.readEntity(DrupalNode.class);
        return getToolFromNode(node);
    }

    private FabTool getToolFromNode(DrupalNode node) {
        String body = node.getBody();
        Document doc = Jsoup.parse(body, fabUrl);

        FabTool tool = new FabTool();
        tool.setId(node.getNid());
        tool.setTitle(node.getTitle());
        tool.setDescription(node.getTeaser());
        tool.setDetails(HTMLHelper.parseBody(doc, false));
        tool.setLink(fabUrl + "/" + node.getPath());
        tool.setLinkToPicture(null);

        return tool;
    }
}
