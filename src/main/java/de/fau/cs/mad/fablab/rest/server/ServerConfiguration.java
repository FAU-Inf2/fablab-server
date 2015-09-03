package de.fau.cs.mad.fablab.rest.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fau.cs.mad.fablab.rest.core.User;
import de.fau.cs.mad.fablab.rest.server.configuration.*;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class containing environment specific parameters
 * specified in the given YAML file when starting the application
 * The mapping between this class and the used yaml file is done by Jackson
 */
class ServerConfiguration extends Configuration
{
    @NotNull
    @Valid
    @JsonProperty
    private SpaceApiConfiguration spaceapi;
    public SpaceApiConfiguration getSpaceApiConfiguration(){ return spaceapi; }

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
    public DataSourceFactory getDatabase() {
        return database;
    }

    @Valid
    @NotNull
    @JsonProperty
    private AdminConfiguration admin = new AdminConfiguration();
    public AdminConfiguration getAdminConfiguration() { return admin; }

    @Valid
    @NotNull
    @JsonProperty
    private OpenErpConfiguration openerp = new OpenErpConfiguration();
    public OpenErpConfiguration getOpenErpConfiguration() { return openerp; }

    @Valid
    @NotNull
    @JsonProperty
    private ICalConfiguration ical = new ICalConfiguration();
    public ICalConfiguration getICalConfiguration() { return ical; }

    @Valid
    @NotNull
    @JsonProperty
    private NewsConfiguration drupalNews = new NewsConfiguration();
    public NewsConfiguration getNewsConfiguration() { return drupalNews; }

    @Valid
    @NotNull
    @JsonProperty
    private GeneralDataConfiguration generalData = new GeneralDataConfiguration();
    public GeneralDataConfiguration getGeneralDataConfiguration() { return generalData; }

    @Valid
    @NotNull
    @JsonProperty
    private AndroidPushConfiguration androidPushConfiguration = new AndroidPushConfiguration();
    public AndroidPushConfiguration getAndroidPushConfiguration() { return androidPushConfiguration; }

    @Valid
    @NotNull
    @JsonProperty
    private ApplePushConfiguration applePushConfiguration = new ApplePushConfiguration();
    public ApplePushConfiguration getApplePushConfiguration() { return applePushConfiguration; }

    @NotNull
    @JsonProperty
    private List<MinimumVersionConfiguration> minimumVersion = new ArrayList<>();
    public List<MinimumVersionConfiguration> getMinimumVersionConfiguration() { return minimumVersion; }

    @NotNull
    @JsonProperty
    private List<User> users = new ArrayList<>();
    public List<User> getUserList() { return users; }

}
