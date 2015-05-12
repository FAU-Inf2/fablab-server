package de.fau.cs.mad.fablab.rest.server;

import de.fau.cs.mad.fablab.rest.server.core.*;
import de.fau.cs.mad.fablab.rest.server.health.DatabaseHealthCheck;
import de.fau.cs.mad.fablab.rest.server.health.HelloFablabHealthCheck;
import de.fau.cs.mad.fablab.rest.server.remote.SpaceAPIService;
import de.fau.cs.mad.fablab.rest.server.resources.*;
import de.fau.cs.mad.fablab.rest.server.security.AdminConstraintSecurityHandler;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

/**
 * The Core of our rest server
 */
class ServerApplication extends Application<ServerConfiguration>
{

    /***
     * used to configure aspects of the application required before the application is run
     * @param bootstrap
     */
    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception
    {
        //create an instance of our HelloFablabResource
        final HelloFablabResource helloFablabResource = new HelloFablabResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );

        final SpaceAPIResource spaceAPIResource = new SpaceAPIResource(
                SpaceAPIService.ENDPOINT
        );




        //Create our basic healthcheck
        final HelloFablabHealthCheck helloFablabHealthCheck =
            new HelloFablabHealthCheck(configuration.getTemplate());

        //add healthcheck and resource to our jersey environment
        environment.healthChecks().register("Hello Fablab template", helloFablabHealthCheck);

        environment.jersey().register(helloFablabResource);
        environment.jersey().register(spaceAPIResource);

        environment.jersey().register(new NewsResource(new NewsDAO(hibernate.getSessionFactory())));
        environment.jersey().register(new ICalResource(new ICalDAO(hibernate.getSessionFactory())));
        environment.jersey().register(new ProductResource(new ProductDAO(hibernate.getSessionFactory())));
        environment.jersey().register(new CartResource(new CartDAO(hibernate.getSessionFactory())));

        environment.healthChecks().register("DBHealthCheck", new DatabaseHealthCheck(hibernate));


        //set the security handler for admin resources
        environment.admin().setSecurityHandler(new AdminConstraintSecurityHandler());
    }

    public static void main(String[] args)
    {
        try
        {
            new ServerApplication().run(args);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public final HibernateBundle<ServerConfiguration> hibernate = new HibernateBundle<ServerConfiguration>(News.class, ICal.class, Product.class, Cart.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

}
