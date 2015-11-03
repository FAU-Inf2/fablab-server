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

    @NotEmpty
    @JsonProperty
    private String gistUrl;

    @NotEmpty
    @JsonProperty
    private String gistUser;

    public boolean validate() {
        if (token == null || token.isEmpty() || apiUrl == null || apiUrl.isEmpty() || gistUrl == null || gistUrl.isEmpty() || gistUser == null || gistUser.isEmpty()) return false;
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

    public String getGistUrl() {
        return gistUrl;
    }

    public void setGistUrl(String gistUrl) {
        this.gistUrl = gistUrl;
    }

    public String getGistUser() {
        return gistUser;
    }

    public void setGistUser(String gistUser) {
        this.gistUser = gistUser;
    }
}
