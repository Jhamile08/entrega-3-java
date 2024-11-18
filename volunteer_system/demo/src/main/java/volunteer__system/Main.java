package volunteer__system;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import volunteer__system.controllers.InscriptionsController;
import volunteer__system.controllers.ProjectsController;
import volunteer__system.controllers.UsersController;
import volunteer__system.entities.Inscription;
import volunteer__system.entities.Project;
import volunteer__system.entities.User;
import volunteer__system.models.InscriptionsModel;
import volunteer__system.models.ProjectsModel;
import volunteer__system.models.UsersModel;
import volunteer__system.models.interfaces.IInscriptionsModel;
import volunteer__system.models.interfaces.IProjectsModel;
import volunteer__system.models.interfaces.IUsersModel;
import volunteer__system.persistence.ConfigDB;
import volunteer__system.utils.Enums.RoleEnum;
import volunteer__system.utils.InputRequester;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ConfigDB connection = new ConfigDB();

        IUsersModel usersModel = new UsersModel(connection);
        var usersController = new UsersController(usersModel);

        IProjectsModel projectsModel = new ProjectsModel(connection);
        var projectsController = new ProjectsController(projectsModel);

        boolean isMenuOpened = true;

        while (isMenuOpened) {
            String menuOptionsMessage = """
                    Welcome!
                    1. register
                    2. login in
                    0. out
                    """;

            var option = InputRequester.requestString(menuOptionsMessage);
            switch (option) {
                case "0" -> isMenuOpened = false;
                case "1" -> registerUser(usersController);
                case "2" -> loginUser(usersController);

            }

        }

    }

    public static void registerUser(UsersController usersController) {
        var name = InputRequester.requestString("Enter the name: ");
        var email = InputRequester.requestString("Enter the email");
        var password = InputRequester.requestString("Enter the password");
        RoleEnum role = selectRole();

        // Check if the email is already registered
        var foundStudent = usersController.findByEmail(email);
        if (foundStudent.isPresent()) {
            JOptionPane.showMessageDialog(null, "Email already exists" + email);
            return;
        }

        // Otherwise register student
        var user = new User(name, email, password, role);
        var registeredStudent = usersController.register(user);
        JOptionPane.showMessageDialog(null, "¡User registred sucessfully!\n\n" + registeredStudent);
        mainMenu(registeredStudent);

    }

    public static void loginUser(UsersController usersController) {
        var email = InputRequester.requestString("Enter the email:");
        var password = InputRequester.requestString("Enter the password:");

        var foundUser = usersController.findByEmail(email);

        if (foundUser.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email not found. Please register first.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        var user = foundUser.get();
        if (!user.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Login exitoso
        JOptionPane.showMessageDialog(null,
                "Welcome back, " + user.getName() + "!\nRole: " + user.getRole(),
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
        mainMenu(user);
    }

    private static RoleEnum selectRole() {
        RoleEnum[] roles = RoleEnum.values();
        String[] roleNames = new String[roles.length];

        for (int i = 0; i < roles.length; i++) {
            roleNames[i] = roles[i].name();
        }

        // Mostrar cuadro de diálogo con roles
        String selectedRole = (String) JOptionPane.showInputDialog(
                null,
                "Select a rol:",
                "Selecter a Rol",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roleNames,
                roleNames[0]);

        if (selectedRole == null) {
            JOptionPane.showMessageDialog(null, "You dont select a rol.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Termina la ejecución si no se selecciona un rol
        }

        // Convertir la selección al valor de `RoleEnum`
        return RoleEnum.valueOf(selectedRole);
    }

    private static void mainMenu(User user) {
        System.out.println("Hello world!");

        ConfigDB connection = new ConfigDB();

        IUsersModel usersModel = new UsersModel(connection);

        IProjectsModel projectsModel = new ProjectsModel(connection);
        var projectsController = new ProjectsController(projectsModel);

        IInscriptionsModel inscriptionsModel = new InscriptionsModel(connection);
        var inscriptionsController = new InscriptionsController(inscriptionsModel);

        var usersController = new UsersController(usersModel);
        if (user.getRole() == RoleEnum.VOLUNTARIO) {
            boolean isMenuOpened = true;

            while (isMenuOpened) {
                String menuOptionsMessage = """
                        Welcome VOLUNTARIO!
                        1. see the projects availables
                        2. create suscription
                        3. see subscriptions
                        0. out
                        """;

                var option = InputRequester.requestString(menuOptionsMessage);
                switch (option) {
                    case "0" -> isMenuOpened = false;
                    case "1" -> seeProject(user);
                    case "2" -> createInscription(inscriptionsController, user);
                    case "3" -> showSubscribedProjects(user);

                }

            }
        } else if (user.getRole() == RoleEnum.PUBLICANTE) {
            boolean isMenuOpened = true;

            while (isMenuOpened) {
                String menuOptionsMessage = """
                        Welcome PUBLICANTE!
                        1. create project
                        2. see projects created
                        0. out
                        """;

                var option = InputRequester.requestString(menuOptionsMessage);
                switch (option) {
                    case "0" -> isMenuOpened = false;
                    case "1" -> createProject(projectsController, user);
                    case "2" -> seeProject(user);

                }
            }
        }
    }

    public static void createProject(ProjectsController projectsController, User user) {
        var title = InputRequester.requestString("Enter the title: ");
        var description = InputRequester.requestString("Enter the description");
        var start_date = InputRequester.requestDate("Enter the start_date(yyyy-mm-dd)");
        var end_date = InputRequester.requestDate("Enter the end_date(yyyy-mm-dd)");
        var created_by = user.getId();

        // Otherwise register student
        var project = new Project(title, description, start_date, end_date, created_by);
        var registeredProject = projectsController.create(project, user);
        JOptionPane.showMessageDialog(null, "Project registred sucessfully!\n\n");

    }

    public static void seeProject(User user) {
        ConfigDB connection = new ConfigDB();

        IProjectsModel projectsModel = new ProjectsModel(connection);
        var projectsController = new ProjectsController(projectsModel);

        // Obtener todos los proyectos
        List<Project> projects = projectsController.findAll();

        if (projects.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay proyectos disponibles.", "Lista de Proyectos",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Project> filteredProjects;
        System.out.println(user.getRole());
        if (user.getRole() == RoleEnum.PUBLICANTE) {
            // Filtrar proyectos creados por el propietario
            filteredProjects = projects.stream()
                    .filter(project -> project.getCreated_by() == user.getId())
                    .toList();
        } else if (user.getRole() == RoleEnum.VOLUNTARIO) {
            // Mostrar todos los proyectos para voluntarios
            filteredProjects = projects;
        } else {
            JOptionPane.showMessageDialog(null, "Rol no reconocido.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si no hay proyectos después de filtrar
        if (filteredProjects.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay proyectos disponibles para tu rol.", "Lista de Proyectos",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construir el mensaje para mostrar los proyectos
        StringBuilder message = new StringBuilder("Lista de Proyectos:\n");
        for (int i = 0; i < filteredProjects.size(); i++) {
            Project project = filteredProjects.get(i);
            message.append(String.format(
                    "Proyecto %d:\n  - Título: %s\n  - Descripción: %s\n  - Inicio: %s\n  - Fin: %s\n\n",
                    i + 1,
                    project.getTitle(),
                    project.getDescription(),
                    project.getStart_date(),
                    project.getEnd_date()));
        }

        // Mostrar los proyectos en un JOptionPane
        JOptionPane.showMessageDialog(null, message.toString(), "Lista de Proyectos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void createInscription(InscriptionsController inscriptionsController, User user) {
        try {
            // Step 1: Ask for the project ID
            var project_id = Integer.parseInt(JOptionPane.showInputDialog("Enter the project id for inscription"));
            project_id += 1;
            var user_id = user.getId(); // Get the user ID from the User object
            var current_date = new Date(); // Get the current date

            // Step 2: Create the Inscription object
            var inscription = new Inscription(user_id, project_id, current_date);

            // Step 3: Call the controller to create the inscription
            boolean isRegistered = inscriptionsController.create(inscription);

            // Step 4: Show success or failure message
            if (isRegistered) {
                JOptionPane.showMessageDialog(null, "Project registered successfully!\n\n", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to register for the project. Please try again.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid project ID.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void showSubscribedProjects(User user) {
        ConfigDB connection = new ConfigDB();
        IProjectsModel projectsModel = new ProjectsModel(connection);
        ProjectsController projectsController = new ProjectsController(projectsModel);
        System.out.println(user.getId());
        // Get the projects the user is subscribed to
        List<Project> subscribedProjects = projectsController.getSubscribedProjects(user.getId());

        if (subscribedProjects.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You are not subscribed to any projects.", "Subscribed Projects",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Build the message to display
        StringBuilder message = new StringBuilder("Subscribed Projects:\n");
        for (int i = 0; i < subscribedProjects.size(); i++) {
            Project project = subscribedProjects.get(i);
            message.append(String.format(
                    "Project %d:\n  - Title: %s\n  - Description: %s\n  - Start: %s\n  - End: %s\n\n",
                    i + 1,
                    project.getTitle(),
                    project.getDescription(),
                    project.getStart_date(),
                    project.getEnd_date()));
        }

        // Show the projects in a JOptionPane
        JOptionPane.showMessageDialog(null, message.toString(), "Subscribed Projects",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
