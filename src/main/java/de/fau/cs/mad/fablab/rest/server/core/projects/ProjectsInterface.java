package de.fau.cs.mad.fablab.rest.server.core.projects;

import de.fau.cs.mad.fablab.rest.core.ProjectImageUpload;
import de.fau.cs.mad.fablab.rest.core.ProjectFile;

public interface ProjectsInterface {
    public String postProject(ProjectFile project);
    public String patchProject(String gistId, ProjectFile project);
    public void deleteProject(String gistId);
    public String commitImage(ProjectImageUpload image);
}
