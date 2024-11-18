package volunteer__system.models;

import java.sql.SQLException;

import volunteer__system.entities.Inscription;
import volunteer__system.models.interfaces.IInscriptionsModel;
import volunteer__system.persistence.ConfigDB;

public class InscriptionsModel implements IInscriptionsModel {
    private final ConfigDB ConfigDB;

    public InscriptionsModel(volunteer__system.persistence.ConfigDB configDB) {
        ConfigDB = configDB;
    }

    @Override
    public Boolean create(Inscription inscription) {
        String sql = "INSERT INTO inscriptions (user_id, project_id) VALUES (?, ?)";

        try (var connection = ConfigDB.openConnection();
                var statement = connection.prepareStatement(sql)) {

            // Set the parameters
            statement.setInt(1, inscription.getUser_id());
            statement.setInt(2, inscription.getProject_id());

            // Execute the update and check if successful
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            // Log and throw exception for better debugging
            System.out.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Error creating inscription: " + e.getMessage(), e);
        }
    }

}
