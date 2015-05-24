package de.fau.cs.mad.fablab.rest.server.security;

import de.fau.cs.mad.fablab.rest.server.configuration.AdminConfiguration;
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
    public AdminMappedLoginService(final AdminConfiguration admin)
    {
        if (admin == null || !admin.validate())
        {
            System.err.println("Missing configuration vars for admin user/pass...");
            System.exit(1);
        }

        putUser(
                admin.getUsername(),
                new Password(admin.getPassword()),
                new String[]{admin.getRole()}
        );
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
