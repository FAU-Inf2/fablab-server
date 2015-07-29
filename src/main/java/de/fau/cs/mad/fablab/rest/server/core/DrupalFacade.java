package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.server.core.drupal.DrupalClient;
import de.fau.cs.mad.fablab.rest.server.core.drupal.DrupalInterface;

import java.util.List;

public class DrupalFacade {

    private final DrupalDAO dao;
    private final DrupalInterface drupalInterface;

    public DrupalFacade(DrupalDAO dao) {
        this.dao = dao;
        this.drupalInterface = DrupalClient.getInstance();
    }

    public FabTool findToolById(Long id) {
        return drupalInterface.findToolById(id);
    }

    public List<FabTool> findAllTools() {
        return drupalInterface.findAllTools();
    }
}
