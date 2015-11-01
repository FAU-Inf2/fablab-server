package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class provides configuration details for the connection to the OpenERP system
 */
public class OpenErpConfiguration {

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private String hostname;

    @NotEmpty
    @JsonProperty
    private String database;

    public boolean validate()
    {
        if (hostname == null || hostname.isEmpty() ||
                password == null || password.isEmpty() ||
                username == null || username.isEmpty() ||
                database == null || database.isEmpty())
            return false;

        return true;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
