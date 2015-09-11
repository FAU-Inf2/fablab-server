package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class ProjectsConfiguration {

    @NotEmpty
    @JsonProperty
    private String token;

    @NotEmpty
    @JsonProperty
    private String apiUrl;

    public boolean validate() {
        if (token == null || token.isEmpty() || apiUrl == null || apiUrl.isEmpty()) return false;
        return true;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
