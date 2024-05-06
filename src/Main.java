import java.sql.*;
import java.util.Scanner;
import DAO.UserDAO;
import model.Utilisateur;

public class Main {


        // Method to generate auto incremented ID
        private static int generateAutoIncrementedId() {
            int newId = 1; // Start from ID 1

            // Check if the id exists in the database
            while (checkIfIdExists(newId)) {
                newId++; // Increment
            }

            return newId;
        }

        private static boolean checkIfIdExists(int id) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                // Establish database connection
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppResaurant", "root", "");

                // Prepare SQL query to check if the ID exists
                String query = "SELECT COUNT(*) FROM Users WHERE id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);

                // Execute the query
                resultSet = statement.executeQuery();

                // Get the result
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Return true if count > 0 (ID exists), false otherwise
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return false; // Return false if an exception occurs or no result is found
        }
    }

