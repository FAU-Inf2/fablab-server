package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Configuration class for Version Check.
 */
public class VersionCheckConfiguration {

    @JsonProperty
    @NotEmpty
    /**
     * Path to yml file containing the minimum version information. Will be parsed using the MinimumVersionFileConfiguration class
     * @see de.fau.cs.mad.fablab.rest.server.configuration.MinimumVersionFileConfiguration
     */
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
