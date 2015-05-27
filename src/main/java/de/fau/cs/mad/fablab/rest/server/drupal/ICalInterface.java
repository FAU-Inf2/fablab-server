package de.fau.cs.mad.fablab.rest.server.drupal;

import java.util.List;

import de.fau.cs.mad.fablab.rest.core.ICal;

public interface ICalInterface {
    public ICal findById(Long id);
    public List<ICal> findAll();
}
