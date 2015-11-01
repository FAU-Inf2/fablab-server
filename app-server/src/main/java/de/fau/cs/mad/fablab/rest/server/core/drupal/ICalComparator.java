package de.fau.cs.mad.fablab.rest.server.core.drupal;

import de.fau.cs.mad.fablab.rest.core.ICal;

import java.util.Comparator;

public class ICalComparator implements Comparator<ICal> {

    @Override
    public int compare(ICal o1, ICal o2) {
        return o1.getStart().compareTo(o2.getStart());
    }
}
