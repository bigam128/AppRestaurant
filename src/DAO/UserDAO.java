package DAO;

import model.Utilisateur;

import java.sql.*;

public class UserDAO {
    private static final String INSERT_USER_SQL = "INSERT INTO Utilisateur (username, password, role_id, nom, prenom, email) VALUES (?, ?, ?, ?,?,?)";

    public static int addUser(Utilisateur user) {
        int generatedUserId = -1;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRoleid());
            preparedStatement.setString(4, user.getNom());
            preparedStatement.setString(5, user.getPrenom());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                generatedUserId = rs.getInt(1);
            }
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding user.");
            e.printStackTrace();
        }
        return generatedUserId;

    }
}

