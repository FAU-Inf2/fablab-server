package de.fau.cs.mad.fablab.rest.server;

import de.fau.cs.mad.fablab.rest.server.health.HelloFablabHealthCheck;
import de.fau.cs.mad.fablab.rest.server.resources.HelloFablabResource;
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

        //Create our basic healthcheck
        final HelloFablabHealthCheck helloFablabHealthCheck =
            new HelloFablabHealthCheck(configuration.getTemplate());

        //add healthcheck and resource to our jersey environment
        environment.healthChecks().register("Hello Fablab template", helloFablabHealthCheck);
        environment.jersey().register(helloFablabResource);
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