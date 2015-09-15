package de.fau.cs.mad.fablab.rest.server.resources;

import de.fau.cs.mad.fablab.rest.api.ProjectsApi;
import de.fau.cs.mad.fablab.rest.core.ProjectImageUpload;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;
import de.fau.cs.mad.fablab.rest.server.core.projects.ProjectsClient;
import de.fau.cs.mad.fablab.rest.server.core.projects.ProjectsInterface;
import de.fau.cs.mad.fablab.rest.server.exceptions.Http500Exception;

public class ProjectsResource implements ProjectsApi {

    private ProjectsInterface projectsInterface;

    public ProjectsResource() {
        projectsInterface = ProjectsClient.getInstance();
    }

    @Override
    public String createProject(ProjectFile project) {
        String gistUrl = projectsInterface.postProject(project);
        if (gistUrl == null) {
            throw new Http500Exception("Project was not created.");
        }
        return gistUrl;
    }

    @Override
    public String updateProject(String gistId, ProjectFile project) {
        String gistUrl = projectsInterface.patchProject(gistId, project);
        if (gistUrl == null) {
            throw new Http500Exception("Project was not patched.");
        }
        return gistUrl;
    }

    @Override
    public String uploadImage(ProjectImageUpload image) {
        String imageUrl = projectsInterface.commitImage(image);
        if (imageUrl == null) {
            throw new Http500Exception("Image was not committed.");
        }
        return imageUrl;
    }
}
