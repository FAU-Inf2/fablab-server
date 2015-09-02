package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fau.cs.mad.fablab.rest.core.PlatformType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Configuration to specify minim required/optional version for a specific platform type.
 */
public class MinimumVersionConfiguration {

    @JsonProperty
    @NotNull
    private PlatformType platform;

    @JsonProperty
    @NotEmpty
    int latestVersionCode;

    @JsonProperty
    @NotEmpty
    String latestVersion;

    @JsonProperty
    @NotEmpty
    int minimumRequiredVersionCode;

    @JsonProperty
    @NotEmpty
    String updateMessage;

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public void setLatestVersionCode(int latestVersionCode) {
        this.latestVersionCode = latestVersionCode;
    }

    public int getMinimumRequiredVersionCode() {
        return minimumRequiredVersionCode;
    }

    public void setMinimumRequiredVersionCode(int minimumRequiredVersionCode) {
        this.minimumRequiredVersionCode = minimumRequiredVersionCode;
    }

    public String getUpdateMessage(){ return updateMessage; }

    public void setUpdateMessage(String updateMessage){ this.updateMessage = updateMessage; }

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformType platform) {
        this.platform = platform;
    }
}
