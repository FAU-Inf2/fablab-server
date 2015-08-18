package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.UserApi;
import de.fau.cs.mad.fablab.rest.core.User;
import io.dropwizard.auth.Auth;

/**
 * Resource to handle User authentication and query information about users (i.e. Roles)
 */
public class UserResource implements UserApi {

    @Override
    public User getUserInfo(@Auth User user) {
        return user;
    }

}
