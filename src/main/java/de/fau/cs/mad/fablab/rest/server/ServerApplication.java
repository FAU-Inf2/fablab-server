package de.fau.cs.mad.fablab.rest.server;

import com.google.common.cache.CacheBuilderSpec;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import de.fau.cs.mad.fablab.rest.server.core.*;
import de.fau.cs.mad.fablab.rest.server.core.drupal.DrupalClient;
import de.fau.cs.mad.fablab.rest.server.core.drupal.ICalClient;
import de.fau.cs.mad.fablab.rest.server.core.drupal.NewsFeedClient;
import de.fau.cs.mad.fablab.rest.server.core.openerp.OpenErpClient;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.AndroidPushManager;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.ApplePushManager;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.PushDAO;
import de.fau.cs.mad.fablab.rest.server.core.pushservice.PushFacade;
import de.fau.cs.mad.fablab.rest.server.core.spaceapi.DoorStateDAO;
import de.fau.cs.mad.fablab.rest.server.health.DatabaseHealthCheck;
import de.fau.cs.mad.fablab.rest.server.managed.UpdateDatabaseManager;
import de.fau.cs.mad.fablab.rest.server.resources.*;
import de.fau.cs.mad.fablab.rest.server.resources.admin.LogResource;
import de.fau.cs.mad.fablab.rest.server.security.AdminConstraintSecurityHandler;
import de.fau.cs.mad.fablab.rest.server.security.SimpleAuthenticator;
import de.fau.cs.mad.fablab.rest.server.tasks.UpdateProductDatabaseTask;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.hibernate.cfg.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.nio.channels.ServerSocketChannel;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Properties;

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

        // add assets for dummy checkout, inventory and product map.
        bootstrap.addBundle(new AssetsBundle("/dummy", "/dummy", null, "dummy.html"));
        bootstrap.addBundle(new AssetsBundle("/productMap", "/productMap", null, "productMap.html"));
        bootstrap.addBundle(new AssetsBundle("/inventories", "/inventories", null, "inventory.html"));
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {

        CacheBuilderSpec.disableCaching();

        exportDatabaseSchema(configuration.getDatabase());

        // add health check and resource to our jersey environment
        environment.healthChecks().register("DBHealthCheck", new DatabaseHealthCheck(hibernate));

        // configure OpenERP client
        OpenErpClient.setConfiguration(configuration.getOpenErpConfiguration());

        // configure ICalClient
        ICalClient.setConfiguration(configuration.getICalConfiguration());

        // configure NewsClient
        NewsFeedClient.setConfiguration(configuration.getNewsConfiguration(), configuration.getGeneralDataConfiguration());

        // configure DrupalClient
        DrupalClient.setConfiguration(configuration.getNewsConfiguration(), configuration.getGeneralDataConfiguration());

        // configure date format for jackson
        //environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        environment.getObjectMapper().setDateFormat(new SimpleDateFormat(Format.DATE_FORMAT));

        // create and register instance of SpaceApiResource
        SpaceApiConfiguration spaceApiConfiguration = configuration.getSpaceApiConfiguration();
        final SpaceAPIResource spaceAPIResource = new SpaceAPIResource(
                spaceApiConfiguration,
                new DoorStateDAO(hibernate.getSessionFactory())
        );
        environment.jersey().register(spaceAPIResource);

        // create some resources
        environment.jersey().register(new NewsResource(new NewsFacade(new NewsDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new ICalResource(new ICalFacade(new ICalDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new DrupalResource(new DrupalFacade(new DrupalDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new ProductResource(new ProductFacade(new ProductDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new CartResource(new CartFacade(new CartDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new CheckoutResource(new CartFacade(new CartDAO(hibernate.getSessionFactory()))));
        environment.jersey().register(new InventoryResource(new InventoryFacade(new InventoryDAO(hibernate.getSessionFactory()))));

        environment.jersey().register(new UserResource());
        environment.jersey().register(new ContactReource());
        environment.jersey().register(new VersionCheckResource(configuration.getMinimumVersionConfiguration()));
        environment.jersey().register(new GeneralDataResource(configuration.getGeneralDataConfiguration()));

        UpdateProductDatabaseTask updateProductDatabaseTask = new UpdateProductDatabaseTask(hibernate.getSessionFactory());
        environment.admin().addTask(updateProductDatabaseTask);
        updateProductDatabaseTask.execute(null, null);

        //set the security handler for admin resources
        environment.admin().setSecurityHandler(new AdminConstraintSecurityHandler(configuration.getAdminConfiguration()));

        // create dummy data
        dummyData.createDummyData(hibernate);

        //Log resource inside admin environment
        final DropwizardResourceConfig dropwizardResourceConfig = new DropwizardResourceConfig(environment.metrics());
        JerseyContainerHolder jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(dropwizardResourceConfig));
        dropwizardResourceConfig.register(LogResource.class);
        environment.admin().addServlet("log admin resource", jerseyContainerHolder.getContainer()).addMapping("/admin/*");

        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // Add AuthenticationService for some special APIs
        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(new SimpleAuthenticator(configuration.getUserList()),
                                                                                "Authentication required",
                                                                                User.class)));

        //PUSH --> ADD MANAGERS TO PushFacade SINGLETON
        PushFacade.getInstance().addPushManager(new AndroidPushManager(configuration.getAndroidPushConfiguration()), PlatformType.ANDROID);
        PushFacade.getInstance().addPushManager(new ApplePushManager(configuration.getApplePushConfiguration()), PlatformType.APPLE);
        PushFacade.getInstance().setDao(new PushDAO(hibernate.getSessionFactory()));
        environment.jersey().register(new PushResource());

        UpdateDatabaseManager updateDatabaseManager = new UpdateDatabaseManager(configuration.getAdminConfiguration(), configuration.getNetworkConfiguration());
        environment.lifecycle().manage(updateDatabaseManager);
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
            FabTool.class,
            Product.class,
            CartServer.class,
            CartEntryServer.class,
            DoorState.class,
            InventoryItem.class,
            PushToken.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

    public void exportDatabaseSchema(DataSourceFactory database) {

        Properties p = new Properties();
        p.putAll(database.getProperties());

        Configuration config = new Configuration();
        config.setProperties(p);

        config.addAnnotatedClass(News.class);
        config.addAnnotatedClass(ICal.class);
        config.addAnnotatedClass(FabTool.class);
        config.addAnnotatedClass(Product.class);
        config.addAnnotatedClass(CartServer.class);
        config.addAnnotatedClass(CartEntryServer.class);
        config.addAnnotatedClass(DoorState.class);
        config.addAnnotatedClass(PushToken.class);
        config.addAnnotatedClass(InventoryItem.class);

        SchemaExporter exporter = new SchemaExporter(config, "src/dist/schema.sql");
        exporter.export();
    }
}

