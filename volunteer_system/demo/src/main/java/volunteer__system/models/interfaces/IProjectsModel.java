package volunteer__system.models.interfaces;

import java.util.List;

import volunteer__system.entities.Project;
import volunteer__system.entities.User;

public interface IProjectsModel {
    Project create(Project project, User user);

    List<Project> seeInscription(int user);

    List<Project> findAll();
}
