package de.fau.cs.mad.fablab.rest.server.security;

import com.google.common.base.Optional;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.fau.cs.mad.fablab.rest.core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * Simple Authenticator, which allows a User that has password = "secret"
 * This should be replaced by Authentication with drupal
 */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {

        if ("secret".equals(credentials.getPassword())) {

            User user = new User(credentials.getUsername(), credentials.getPassword());

            user.addRole(Roles.USER);

            if (user.getUsername().equals("inventory"))
                user.addRole(Roles.INVENTORY);

            if (user.getUsername().equals("admin"))
                user.addRole(Roles.ADMIN);

            return Optional.of(user);
        }

        return Optional.absent();
    }
}
