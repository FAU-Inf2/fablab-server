package de.fau.cs.mad.fablab.rest.server.security;

import de.fau.cs.mad.fablab.rest.server.configuration.AdminConfiguration;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;

import org.eclipse.jetty.util.security.Constraint;

/**
 * This class helps protecting the resources which belong to the admin role.
 * Implementation is based on the examples found at {@url https://github.com/dropwizard/dropwizard/issues/593}
 * and {@url http://stackoverflow.com/questions/12987911/restricting-dropwizard-admin-page}
 */
public class AdminConstraintSecurityHandler extends ConstraintSecurityHandler
{
    private static final String ADMIN_ROLE = "admin";

    public AdminConstraintSecurityHandler(AdminConfiguration admin)
    {
        //create constraint for admin with authentication
        final Constraint constraint = new Constraint(Constraint.__BASIC_AUTH, ADMIN_ROLE);
        constraint.setRoles(new String[]{ADMIN_ROLE});
        constraint.setAuthenticate(true);

        //create the mapping for this constraint
        final ConstraintMapping adminConstraintMapping = new ConstraintMapping();
        adminConstraintMapping.setConstraint(constraint);
        adminConstraintMapping.setPathSpec("/*");
        setAuthenticator(new BasicAuthenticator());
        addConstraintMapping(adminConstraintMapping);
        setLoginService(new AdminMappedLoginService(admin));
    }
}
