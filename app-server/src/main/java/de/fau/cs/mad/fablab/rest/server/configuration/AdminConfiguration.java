package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class represents a user, that is allowed to use the admin panel.
 */
public class AdminConfiguration {

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private String role;

    public boolean validate()
    {
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty())
            return false;

        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
