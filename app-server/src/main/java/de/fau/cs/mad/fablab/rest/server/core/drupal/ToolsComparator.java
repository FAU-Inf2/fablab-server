package de.fau.cs.mad.fablab.rest.server.core.drupal;

import de.fau.cs.mad.fablab.rest.core.FabTool;

import java.util.Comparator;

public class ToolsComparator implements Comparator<FabTool> {

    @Override
    public int compare(FabTool t1, FabTool t2) {
        return t1.getTitle().compareTo(t2.getTitle());
    }
}
