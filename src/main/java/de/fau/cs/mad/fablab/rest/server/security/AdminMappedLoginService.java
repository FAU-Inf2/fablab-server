package de.fau.cs.mad.fablab.rest.server.security;

import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Password;

import java.io.IOException;

/**
 * Admin Login Service
 * The userAdmin / userPassword strings have to be stored as environment vars
 */
public class AdminMappedLoginService extends MappedLoginService
{
    //Keys to get the corresponding environment variables
    private static final String ADMIN_USERNAME_KEY = "adminUsername";
    private static final String ADMIN_PASSWORD_KEY = "adminPassword";

    public AdminMappedLoginService(final String role)
    {
        String adminUsername = System.getenv().get(ADMIN_USERNAME_KEY);
        String adminPassword = System.getenv().get(ADMIN_PASSWORD_KEY);

        if (adminUsername == null || adminPassword == null)
        {
            System.err.println("Missing environment vars for admin user/pass...");
            System.exit(1);
        }
        putUser(adminUsername, new Password(adminPassword), new String[]{role});
    }

    @Override
    public String getName()
    {
        return "Admin Login Service";
    }

    @Override
    protected UserIdentity loadUser(String username)
    {
        return null;
    }

    @Override
    protected void loadUsers() throws IOException
    {
    }
}
