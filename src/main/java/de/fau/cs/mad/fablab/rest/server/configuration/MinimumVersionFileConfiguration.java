package de.fau.cs.mad.fablab.rest.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fau.cs.mad.fablab.rest.core.PlatformType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * YML file which contains configuration for minimum version
 */
public class MinimumVersionFileConfiguration {

    @JsonProperty
    @NotNull
    private List<MinimumVersionConfiguration> minimumVersion;

    public List<MinimumVersionConfiguration> getMinimumVersion() {
        return minimumVersion;
    }

    public void setMinimumVersion(List<MinimumVersionConfiguration> minimumVersion) {
        this.minimumVersion = minimumVersion;
    }

    public MinimumVersionConfiguration findConfigForPlatform(PlatformType platform) {
        for (MinimumVersionConfiguration config : minimumVersion) {
            if (config.getPlatform() == platform)
                return config;
        }
        return null;
    }
}
