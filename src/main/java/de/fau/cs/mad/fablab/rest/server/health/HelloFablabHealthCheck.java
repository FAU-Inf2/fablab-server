package de.fau.cs.mad.fablab.rest.server.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Health-Check for our application. Could be called by -url-/healthcheck
 * if all registered healthchecks pass, 200 OK is returned. If any health check fails,
 * 500 Internal Server error is returned
 */
public class HelloFablabHealthCheck extends HealthCheck
{
    private final String template;

    public HelloFablabHealthCheck(String template) {
        this.template = template;
    }

    /***
     * Minimal health check to check our very basic template
     * @return result.healthy if health check succeeds, Result.unhealthy otherwise
     * @throws Exception which will let the test fail
     */
    @Override
    protected Result check() throws Exception
    {
        final String helloWorld = String.format(template, "TEST");
        if (!helloWorld.contains("TEST")) {
            return Result.unhealthy("template doesn't include our test value");
        }
        return Result.healthy();
    }
}
