package de.fau.cs.mad.fablab.rest.server.managed;

import de.fau.cs.mad.fablab.rest.server.configuration.AdminConfiguration;
import io.dropwizard.lifecycle.Managed;
import it.sauronsoftware.cron4j.Scheduler;

public class UpdateDatabaseManager implements Managed {

    final Scheduler scheduler;
    final String username;
    final String password;
    int port = -1;

    public UpdateDatabaseManager(AdminConfiguration adminConfiguration) {

        System.out.println("Created DatabaseManager");
        this.username = adminConfiguration.getUsername();
        this.password = adminConfiguration.getPassword();

        // Creates a Scheduler instance.
        scheduler = new Scheduler();
        // Schedule a once-a-minute task.
        scheduler.schedule("* * * * *", new Runnable() {
            public void run(){
                System.out.println("Another minute ticked away... "+ username+ " "+password+ " "+port);
            }
        });
    }

    public void setPort(int port) throws Exception {
        this.port = port;
        start();
    }

    @Override
    public void start() throws Exception {
        if(port != -1){
            scheduler.start();
        }
    }

    @Override
    public void stop() throws Exception {
        scheduler.stop();
    }
}
