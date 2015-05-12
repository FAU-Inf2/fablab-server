package de.fau.cs.mad.fablab.rest.server.health;
import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.hibernate.HibernateBundle;

/**
 * Created by EE on 11.05.15.
 */
public class DatabaseHealthCheck extends HealthCheck {

    private final HibernateBundle hibernate;

    public DatabaseHealthCheck(HibernateBundle hibernate) {
        this.hibernate = hibernate;
    }

    @Override
    protected Result check() throws Exception {
        if(!hibernate.getSessionFactory().isClosed()) {
            return Result.healthy();
        } else {
            return Result.unhealthy("Cannot connect to database");
        }
    }
}
