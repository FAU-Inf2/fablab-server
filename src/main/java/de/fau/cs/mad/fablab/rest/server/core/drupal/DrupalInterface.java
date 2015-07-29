package de.fau.cs.mad.fablab.rest.server.core.drupal;

import de.fau.cs.mad.fablab.rest.core.FabTool;

import java.util.List;

public interface DrupalInterface {

    List<FabTool> findAllTools();
    FabTool findToolById(long id);
}
