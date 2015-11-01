package de.fau.cs.mad.fablab.rest.server.resources;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import de.fau.cs.mad.fablab.rest.api.VersionCheckApi;
import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.UpdateStatus;
import de.fau.cs.mad.fablab.rest.server.configuration.MinimumVersionConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.MinimumVersionFileConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.VersionCheckConfiguration;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http404Exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Resource to check if newer application version is available.
 */
public class VersionCheckResource  implements VersionCheckApi {

    VersionCheckConfiguration config;
    MinimumVersionFileConfiguration minimumVersionConfiguration;

    public VersionCheckResource(VersionCheckConfiguration versionCheckConfiguration) {
        config = versionCheckConfiguration;
        updateConfiguration(new File(config.getPath()));
    }

    @Override
    public UpdateStatus checkVersion(PlatformType deviceType, int currentVersionCode) {

        UpdateStatus status = new UpdateStatus();
        MinimumVersionConfiguration config = findConfigForPlatform(deviceType);

        status.setLatestVersion(config.getLatestVersion());
        status.setLatestVersionCode(config.getLatestVersionCode());
        status.setOldVersionCode(currentVersionCode);
        status.setUpdateMessage(config.getUpdateMessage());

        if (currentVersionCode < config.getLatestVersionCode()) {
            if (currentVersionCode < config.getMinimumRequiredVersionCode()) {
                status.setUpdateAvailable(UpdateStatus.UpdateAvailability.Required);
            } else {
                status.setUpdateAvailable(UpdateStatus.UpdateAvailability.Optional);
            }
        }
        else {
            status.setUpdateAvailable(UpdateStatus.UpdateAvailability.NotAvailable);
            status.setUpdateMessage("No Update available");
        }

        return status;
    }

    private MinimumVersionConfiguration findConfigForPlatform(PlatformType platform) {
        MinimumVersionConfiguration config = minimumVersionConfiguration.findConfigForPlatform(platform);

        if (config == null)
            throw new Http404Exception("Platform not found.");

        return config;
    }

    public void updateConfiguration(File file) {
        System.out.println("Reading version check configuration from " + file.getAbsoluteFile());
        Yaml yaml = new Yaml();
        try {
            minimumVersionConfiguration = yaml.loadAs(new FileInputStream(file), MinimumVersionFileConfiguration.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
