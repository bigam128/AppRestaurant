package DAO;

import model.Utilisateur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private static final String INSERT_USER_SQL = "INSERT INTO Users (username, password, role_id, name) VALUES (?, ?, ?, ?)";

    public void addUser(Utilisateur user) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRoleid());
            preparedStatement.setString(4, user.getName());
            preparedStatement.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding user.");
            e.printStackTrace();
        }
    }
}
