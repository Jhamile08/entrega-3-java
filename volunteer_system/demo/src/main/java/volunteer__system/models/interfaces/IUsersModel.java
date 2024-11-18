package volunteer__system.models.interfaces;

import java.util.List;
import java.util.Optional;

import volunteer__system.entities.User;

public interface IUsersModel {
    User register(User user);

    User login(String email, String password);

    List<User> findAll();

    Optional<User> findById(int userId);

    Optional<User> findByEmail(String userEmail);

}
