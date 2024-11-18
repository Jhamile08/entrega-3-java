package volunteer__system.controllers;

import java.util.List;
import java.util.Optional;

import volunteer__system.entities.User;
import volunteer__system.models.interfaces.IUsersModel;

public class UsersController {
    private final IUsersModel usersModel;

    public UsersController(IUsersModel usersModel) {
        this.usersModel = usersModel;
    }

    public User register(User user) {
        return this.usersModel.register(user);
    }

    public String login(String email, String password) {
        User user = usersModel.login(email, password);

        if (user != null) {
            // Usuario encontrado, devolver una respuesta adecuada
            return String.format(
                    "Bienvenido, %s! Tu rol es: %s",
                    user.getName(),
                    user.getRole().name());
        } else {
            // Credenciales incorrectas
            return "Error: Credenciales incorrectas o usuario no encontrado.";
        }
    }

    public List<User> findAll() {
        return this.usersModel.findAll();
    }

    public Optional<User> findByEmail(String userEmail) {
        return this.usersModel.findByEmail(userEmail);
    }

    public Optional<User> findById(int userId) {
        return this.usersModel.findById(userId);
    }

}
