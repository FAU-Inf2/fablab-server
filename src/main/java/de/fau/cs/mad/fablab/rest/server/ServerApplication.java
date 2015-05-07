package de.fau.cs.mad.fablab.rest.server;

import de.fau.cs.mad.fablab.rest.server.health.HelloFablabHealthCheck;
import de.fau.cs.mad.fablab.rest.server.remote.SpaceAPIService;
import de.fau.cs.mad.fablab.rest.server.resources.HelloFablabResource;
import de.fau.cs.mad.fablab.rest.server.resources.SpaceAPIResource;
import de.fau.cs.mad.fablab.rest.server.security.AdminConstraintSecurityHandler;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // nothing to do yet
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
}
