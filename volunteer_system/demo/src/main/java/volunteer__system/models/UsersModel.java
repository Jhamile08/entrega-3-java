package volunteer__system.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import volunteer__system.entities.User;
import volunteer__system.models.interfaces.IUsersModel;
import volunteer__system.persistence.ConfigDB;
import volunteer__system.utils.Enums.RoleEnum;

public class UsersModel implements IUsersModel {

    private final ConfigDB configDB;

    public UsersModel(ConfigDB configDB) {
        this.configDB = configDB;
    }

    @Override
    public User register(User user) {
        var connection = ConfigDB.openConnection();

        var sql = """
                INSERT INTO users (name, email, password, role)
                VALUES (?, ?, ?, ?);
                """;
        try (var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().name());

            statement.execute();

            var resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                var generatedId = resultSet.getInt(1);
                user.setId(generatedId);
            } else {
                throw new SQLException("Couldn't register user.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        } finally {
            ConfigDB.closeConnection();
        }
        return user;
    }

    @Override
    public User login(String email, String password) {
        var connection = ConfigDB.openConnection();

        var sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = ? AND password = ?;
                """;
        try (var statament = connection.prepareStatement(sql)) {
            statament.setString(1, email);
            statament.setString(2, password);

            var resultSet = statament.executeQuery();

            if (resultSet.next()) {
                // Convertir el valor de "role" a RoleEnum
                RoleEnum role = RoleEnum.valueOf(resultSet.getString("role").toUpperCase());

                // Crear y devolver el objeto User
                var user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        role);

                user.setId(resultSet.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar sesión", e);
        } finally {
            ConfigDB.closeConnection();
        }
        return null; // Usuario no encontrado
    }

    @Override
    public List<User> findAll() {
        var connection = ConfigDB.openConnection();
        var sql = """
                SELECT id, name, email, role
                FROM users
                """;
        var userList = new ArrayList<User>();

        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                RoleEnum role = RoleEnum.valueOf(resultSet.getString("role").toUpperCase());
                var id = resultSet.getInt("id");
                var name = resultSet.getString("name");
                var email = resultSet.getString("email");
                var user = new User(id, name, email, role);
                userList.add(user);
            }
            resultSet.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        configDB.closeConnection();
        return userList.stream().toList();
    }

    public Optional<User> findByEmail(String email) {
        var connection = ConfigDB.openConnection();
        var sql = "SELECT * FROM users WHERE email = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        RoleEnum.valueOf(resultSet.getString("role")));
                user.setId(resultSet.getInt("id"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying user by email", e);
        } finally {
            ConfigDB.closeConnection();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
