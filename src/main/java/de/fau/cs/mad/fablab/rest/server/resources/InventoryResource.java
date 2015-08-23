
package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.InventoryApi;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.cs.mad.fablab.rest.server.core.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;



public class InventoryResource implements InventoryApi {

    private final InventoryFacade facade;

    public InventoryResource(InventoryFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public InventoryItem add(@Auth User user, InventoryItem obj) {
        return this.facade.create(obj);
    }

    @UnitOfWork
    @Override
    public List<InventoryItem> getAll() {
        return this.facade.getAll();
    }

    @UnitOfWork
    @Override
    public Boolean deleteAll(@Auth User user) {
        return this.facade.deleteAll();
    }
}
