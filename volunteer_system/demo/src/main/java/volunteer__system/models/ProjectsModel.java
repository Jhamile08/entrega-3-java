package volunteer__system.models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import volunteer__system.entities.Project;
import volunteer__system.entities.User;
import volunteer__system.models.interfaces.IProjectsModel;
import volunteer__system.persistence.ConfigDB;

public class ProjectsModel implements IProjectsModel {
    private final ConfigDB configDB;

    public ProjectsModel(ConfigDB configDB) {
        this.configDB = configDB;
    }

    @Override
    public Project create(Project project, User user) {
        var connection = ConfigDB.openConnection();

        var sql = """
                INSERT INTO projects (title, description, start_date, end_date, created_by)
                VALUES (?,?,?,?,?)
                """;

        try (var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Convertir fechas a java.sql.Date
            Date sqlStartDate = new Date(project.getStart_date().getTime());
            Date sqlEndDate = new Date(project.getEnd_date().getTime());

            // Asignar parámetros al PreparedStatement
            statement.setString(1, project.getTitle());
            statement.setString(2, project.getDescription());
            statement.setDate(3, sqlStartDate);
            statement.setDate(4, sqlEndDate);
            statement.setInt(5, user.getId());

            // Ejecutar la consulta
            int rowsAffected = statement.executeUpdate();

            // Validar si se insertó correctamente
            if (rowsAffected > 0) {
                var generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getInt(1)); // Establecer el ID generado
                }
            } else {
                throw new RuntimeException("No se pudo insertar el proyecto.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        } finally {
            ConfigDB.closeConnection();
        }

        return project;
    }

    @Override
    public List<Project> findAll() {
        var connection = ConfigDB.openConnection();
        var sql = """
                SELECT title, description, start_date, end_date,created_by
                FROM projects
                """;
        var projectList = new ArrayList<Project>();

        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                var title = resultSet.getString("title");
                var description = resultSet.getString("description");
                var start_date = resultSet.getDate("start_date");
                var end_date = resultSet.getDate("end_date");
                var created_by = resultSet.getInt("created_by");
                var project = new Project(title, description, start_date, end_date, created_by);
                projectList.add(project);
            }
            resultSet.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        configDB.closeConnection();
        return projectList.stream().toList();
    }

    @Override
    public List<Project> seeInscription(int user) {
        List<Project> projects = new ArrayList<>();
        var sql = """
                    SELECT p.id, p.title, p.description, p.start_date, p.end_date, p.created_by
                    FROM projects p
                    JOIN inscriptions i ON p.id = i.project_id
                    WHERE i.user_id = ?
                """;

        try (var connection = ConfigDB.openConnection();
                var statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user);

            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Project project = new Project();
                    project.setId(resultSet.getInt("id"));
                    project.setTitle(resultSet.getString("title"));
                    project.setDescription(resultSet.getString("description"));
                    project.setStart_date(resultSet.getDate("start_date"));
                    project.setEnd_date(resultSet.getDate("end_date"));
                    project.setCreated_by(resultSet.getInt("created_by"));
                    projects.add(project);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching subscribed projects: " + e.getMessage(), e);
        }

        return projects;
    }

}
