package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.core.PlatformType;
import de.fau.cs.mad.fablab.rest.api.VersionCheckApi;
import de.fau.cs.mad.fablab.rest.core.UpdateStatus;

/**
 * Resource to check if newer application version is available.
 */
public class VersionCheckResource  implements VersionCheckApi {

    @Override
    public UpdateStatus checkVersion(PlatformType deviceType, int currentVersion) {
        UpdateStatus status = new UpdateStatus();

        status.setLatestVersion("v1.4");
        status.setLatestVersionCode(4);
        status.setOldVersionCode(currentVersion);

        if (currentVersion <= 0) {
            status.setUpdateAvailable(UpdateStatus.UpdateAvailability.Required);
            status.setUpdateMessage("Update is required");
        }
        else if (currentVersion <= 3) {
            status.setUpdateAvailable(UpdateStatus.UpdateAvailability.Optional);
            status.setUpdateMessage("Update is optional");
        }
        else {
            status.setUpdateAvailable(UpdateStatus.UpdateAvailability.NotAvailable);
            status.setUpdateMessage("No Update available.");
        }

        return status;
    }
}
