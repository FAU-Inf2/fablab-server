package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ProjectsApi;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;
import de.fau.cs.mad.fablab.rest.server.configuration.ProjectsConfiguration;
import de.fau.cs.mad.fablab.rest.server.core.drupal.DrupalInterface;
import de.fau.cs.mad.fablab.rest.server.core.projects.ProjectsClient;
import de.fau.cs.mad.fablab.rest.server.core.projects.ProjectsInterface;

public class ProjectsResource implements ProjectsApi {

    private ProjectsInterface projectsInterface;

    public ProjectsResource() {
        projectsInterface = ProjectsClient.getInstance();
    }

    @Override
    public void createProject(ProjectFile project) {
        projectsInterface.postProject(project);
    }
}
