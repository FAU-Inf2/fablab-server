package de.fau.cs.mad.fablab.rest.server.security;

import com.google.common.base.Optional;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.fau.cs.mad.fablab.rest.core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.List;

/**
 * Simple Authenticator, which allows a User that has password = "secret"
 * This should be replaced by Authentication with drupal
 */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {

    private List<User> users;

    public SimpleAuthenticator (List<User> userList) {
        this.users = userList;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {

        for (User u : users) {

            if (u.getUsername().equals(credentials.getUsername()) &&
                u.getPassword().equals(credentials.getPassword())) {
                return Optional.of(u);
            }
        }

        return Optional.absent();
    }
}
