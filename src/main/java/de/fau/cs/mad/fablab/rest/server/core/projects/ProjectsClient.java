package de.fau.cs.mad.fablab.rest.server.core.projects;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;
import de.fau.cs.mad.fablab.rest.core.ProjectImageUpload;
import de.fau.cs.mad.fablab.rest.server.configuration.ProjectsConfiguration;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

enum Method {
    POST, PATCH
}

public class ProjectsClient implements ProjectsInterface {

    private static ProjectsInterface instance;
    private static ProjectsConfiguration config = null;

    private String token;
    private String apiUrl;
    private String gistUrl;
    private String gistUser;

    /***
     * Singleton getInstance()
     * @return instance
     */
    public static ProjectsInterface getInstance() {
        if (instance == null) {
            instance = new ProjectsClient();
        }
        return instance;
    }

    /***
     * Checks for valid configuration and sets necessary urls
     * If any environment variable is missing, it will shutdown the whole application with exit code 1
     */
    private ProjectsClient() {
        if (config == null || !config.validate()) {
            System.err.println("ERROR while initializing NewsClient. Configuration vars missing.\n" +
                    "The configuration (url, port and endpoint) has to be set \n " +
                    "using the class NewsConfiguration.\n");
            System.exit(1);
        }

        token = config.getToken();
        apiUrl = config.getApiUrl();
        gistUrl = config.getGistUrl();
        gistUser = config.getGistUser();
    }

    public static void setConfiguration(ProjectsConfiguration c) {
        config = c;
    }

    /**
     * Posts a new gist to github
     *
     * @param project Object with the data for the gist
     * @return URL to gist
     */
    @Override
    public String postProject(ProjectFile project) {

        JSONObject createGist = getJSONObject(Method.POST, project.getDescription(), project.getFilename(), project.getContent());
        GistResponse response = pushToGitHub(Method.POST, apiUrl, createGist);

        return response.getHtml_url();
    }

    /**
     * Patches an existing gist on github
     *
     * @param gistId id of the gist
     * @param project Object with the data for the gist
     * @return URL to gist
     */
    @Override
    public String patchProject(String gistId, ProjectFile project) {

        JSONObject updateGist = getJSONObject(Method.PATCH, project.getDescription(), project.getFilename(), project.getContent());
        GistResponse response = pushToGitHub(Method.PATCH, apiUrl + "/" + gistId ,updateGist);

        return response.getHtml_url();
    }

    @Override
    public String commitImage(ProjectImageUpload image) {
        String path = null;
        try {
            path = writeImageToDisk(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "zImg_" + image.getFilename();
        String[] args = {"scripts/imgToGist.sh", token, image.getRepoId(), path, filename};
        try {
            Runtime.getRuntime().exec(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // now construct the filename and return it
        return gistUrl + gistUser + "/" + image.getRepoId() + "/raw/" + filename;
    }

    /**
     * pushed the given jsonObject to GitHub
     * @param targetUrl URL to push to
     * @param jsonObject
     * @return the response from github
     */
    private GistResponse pushToGitHub(Method method, String targetUrl, JSONObject jsonObject) {
        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        Invocation invocation = client.target(targetUrl).request(MediaType.APPLICATION_JSON).header("Authorization", "token " + token).buildPost(Entity.entity(jsonObject, MediaType.APPLICATION_JSON));
        Response jsonResponse = invocation.invoke();

        if (method == Method.PATCH) {
            if (jsonResponse.getStatus() != 200) {
                throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + apiUrl);
            }
        } else if (method == Method.POST) {
            if (jsonResponse.getStatus() != 201) {
                throw new RuntimeException("Failed: HTTP Error: " + jsonResponse.getStatus() + ". URL was " + apiUrl);
            }
        }

        return jsonResponse.readEntity(GistResponse.class);
    }

    private JSONObject getJSONObject(Method method, String description, String filename, String content) {
        JSONObject contents = new JSONObject();
        contents.put("content", content);

        JSONObject file = new JSONObject();
        file.put(filename, contents);

        JSONObject createGist = new JSONObject();
        createGist.put("description", description);
        if (method == Method.POST) createGist.put("public", "true");
        createGist.put("files", file);

        return createGist;
    }

    private String writeImageToDisk(ProjectImageUpload image) throws IOException {
        //byte[] data = Base64.decodeBase64(image.getData());
        String path = "/tmp/" + image.getFilename();
        OutputStream stream = new FileOutputStream(new File(path));
        stream.write(image.getData());
        stream.close();
        return path;
    }
}
