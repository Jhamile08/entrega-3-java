package volunteer__system.controllers;

import java.util.List;

import volunteer__system.entities.Project;
import volunteer__system.entities.User;
import volunteer__system.models.interfaces.IProjectsModel;

public class ProjectsController {
    private final IProjectsModel projectsModel;

    public ProjectsController(IProjectsModel projectsModel) {
        this.projectsModel = projectsModel;
    }

    public Project create(Project project, User user) {
        return this.projectsModel.create(project, user);
    }

    public List<Project> findAll() {
        return this.projectsModel.findAll();
    }

    public List<Project> getSubscribedProjects(int user) {
        return projectsModel.seeInscription(user);
    }

}
