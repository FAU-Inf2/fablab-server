package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Provides configuration options for Drupal-API for News
 */
public class NewsConfiguration {

    @NotEmpty
    @JsonProperty
    private String faburl;

    @NotEmpty
    @JsonProperty
    private String feedurl;

    @NotEmpty
    @JsonProperty
    private String url;

    @NotEmpty
    @JsonProperty
    private String port;

    @NotEmpty
    @JsonProperty
    private String nodeEndpoint;

    public boolean validate() {
        if (nodeEndpoint == null || nodeEndpoint.isEmpty() || port == null || port.isEmpty() || url == null || url.isEmpty() || faburl == null || faburl.isEmpty() || feedurl == null || feedurl.isEmpty()) return false;
        return true;
    }

    public String getNodeEndpoint() {
        return nodeEndpoint;
    }

    public void setNodeEndpoint(String nodeEndpoint) {
        this.nodeEndpoint = nodeEndpoint;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFaburl() {
        return faburl;
    }

    public void setFaburl(String faburl) {
        this.faburl = faburl;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }
}
