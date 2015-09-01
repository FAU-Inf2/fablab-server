package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.VersionCheckApi;
import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.core.UpdateStatus;
import de.fau.cs.mad.fablab.rest.server.configuration.MinimumVersionConfiguration;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http404Exception;

import java.util.List;

/**
 * Resource to check if newer application version is available.
 */
public class VersionCheckResource  implements VersionCheckApi {

    List<MinimumVersionConfiguration> minimumVersionConfigurationList;

    public VersionCheckResource(List<MinimumVersionConfiguration> configList) {
        minimumVersionConfigurationList = configList;
    }

    @Override
    public UpdateStatus checkVersion(PlatformType deviceType, int currentVersionCode) {

        UpdateStatus status = new UpdateStatus();
        MinimumVersionConfiguration config = findConfigForPlatform(deviceType);

        status.setLatestVersion(config.getLatestVersion());
        status.setLatestVersionCode(config.getLatestVersionCode());
        status.setOldVersionCode(currentVersionCode);

        if (currentVersionCode < config.getLatestVersionCode()) {
            if (currentVersionCode < config.getMinimumRequiredVersionCode()) {
                status.setUpdateAvailable(UpdateStatus.UpdateAvailability.Required);
                status.setUpdateMessage("Update is required");
            } else {
                status.setUpdateAvailable(UpdateStatus.UpdateAvailability.Optional);
                status.setUpdateMessage("Update is optional");
            }
        }
        else {
            status.setUpdateAvailable(UpdateStatus.UpdateAvailability.NotAvailable);
            status.setUpdateMessage("No Update available");
        }

        return status;
    }

    private MinimumVersionConfiguration findConfigForPlatform(PlatformType platform) {
        for (MinimumVersionConfiguration config : minimumVersionConfigurationList) {
            if (config.getPlatform() == platform)
                return config;

        }
        throw new Http404Exception("Platform not found.");
    }
}
