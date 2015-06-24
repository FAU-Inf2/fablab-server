package de.fau.cs.mad.fablab.rest.server;

import com.google.common.cache.CacheBuilderSpec;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.cs.mad.fablab.rest.server.configuration.PushServiceConfiguration;
import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import de.fau.cs.mad.fablab.rest.server.core.*;
import de.fau.cs.mad.fablab.rest.server.core.drupal.ICalClient;
import de.fau.cs.mad.fablab.rest.server.core.drupal.NewsFeedClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.PushResource;
import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.fau.cs.mad.fablab.rest.server.health.DatabaseHealthCheck;
import de.fau.cs.mad.fablab.rest.server.health.HelloFablabHealthCheck;
import de.fau.cs.mad.fablab.rest.server.resources.*;
import de.fau.cs.mad.fablab.rest.server.resources.admin.LogResource;
import de.fau.cs.mad.fablab.rest.server.security.AdminConstraintSecurityHandler;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.servlet.ServletContainer;

import java.text.SimpleDateFormat;

/**
 * The Core of our rest server
 */
class ServerApplication extends Application<ServerConfiguration> {

    /**
     * used to configure aspects of the application required before the application is run
     *
     * @param bootstrap bootstrap server configuration
     */
    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {

        bootstrap.addBundle(hibernate);
        //enables the use of environment variables in yaml config file
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor()
        ));

        bootstrap.addBundle(new AssetsBundle("/checkoutDummy", "/checkoutDummy", "index.html"));

    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {
        // Create our basic health check
        final HelloFablabHealthCheck helloFablabHealthCheck =
                new HelloFablabHealthCheck(configuration.getTemplate());


        CacheBuilderSpec.disableCaching();


        // add health check and resource to our jersey environment
        environment.healthChecks().register("Hello Fablab template", helloFablabHealthCheck);
        environment.healthChecks().register("DBHealthCheck", new DatabaseHealthCheck(hibernate));

        // configure OpenERP client
        OpenErpClient.setConfiguration(configuration.getOpenErpConfiguration());

        // configure ICalClient
        ICalClient.setConfiguration(configuration.getICalConfiguration());

        // configure NewsClient
        NewsFeedClient.setConfiguration(configuration.getNewsConfiguration());

        // configure date format for jackson
        //environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        environment.getObjectMapper().setDateFormat(new SimpleDateFormat(Format.DATE_FORMAT));


        // create an instance of our HelloFablabResource
        final HelloFablabResource helloFablabResource = new HelloFablabResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(helloFablabResource);

        // create and register instance of SpaceApiResource
        PushServiceConfiguration pushServiceConfiguration = configuration.getPushServiceConfiguration();
        SpaceApiConfiguration spaceApiConfiguration = configuration.getSpaceApiConfiguration();
        final SpaceAPIResource spaceAPIResource = new SpaceAPIResource(
                pushServiceConfiguration,
                spaceApiConfiguration,
                hibernate.getSessionFactory()
        );
        environment.jersey().register(spaceAPIResource);

        // create some resources
        environment.jersey().register(new NewsResource(new NewsFacade(new NewsDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new ICalResource(new ICalFacade(new ICalDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new ProductResource(new ProductFacade(new ProductDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new CartResource(new CartFacade(new CartDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new PushResource(pushServiceConfiguration, hibernate.getSessionFactory()));
        environment.jersey().register(new CheckoutResource(new CartFacade(new CartDAO(hibernate.getSessionFactory()))));

        //set the security handler for admin resources
        environment.admin().setSecurityHandler(new AdminConstraintSecurityHandler(configuration.getAdminConfiguration()));

        // create dummy data
        dummyData.createDummyData(hibernate);

        //Log resource inside admin environment
        final DropwizardResourceConfig dropwizardResourceConfig = new DropwizardResourceConfig(environment.metrics());
        JerseyContainerHolder jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(dropwizardResourceConfig));
        dropwizardResourceConfig.register(LogResource.class);
        environment.admin().addServlet("log admin resource", jerseyContainerHolder.getContainer()).addMapping("/admin/*");

    }

    public static void main(String[] args) {
        try {
            new ServerApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final HibernateBundle<ServerConfiguration> hibernate = new HibernateBundle<ServerConfiguration>(
            News.class,
            ICal.class,
            Product.class,
            Cart.class,
            CartServer.class,
            CartEntry.class,
            CartEntryServer.class,
            DoorState.class,
            RegistrationId.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
            return configuration.getDatabase();
        }
    };
}

