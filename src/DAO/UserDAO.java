package DAO;

import frames.ManagementUtilisateur;
import model.Commande;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class UserDAO  {
    private static final String INSERT_USER_SQL = "INSERT INTO Utilisateur (username, password, role_id, nom, prenom, email) VALUES (?, ?, ?, ?,?,?)";

    public static int addUser(Utilisateur user) {
        int autodId = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRoleid());
            preparedStatement.setString(4, user.getNom());
            preparedStatement.setString(5, user.getPrenom());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                autodId = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autodId;
    }
    public static void deleteUserByUsername(String username) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement delete = connection.prepareStatement("DELETE FROM Utilisateur WHERE username = ?")) {


            delete.setString(1, username);


            int rowsDeleted = delete.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully.");
                rafraichirId();
            } else {
                System.out.println("Can't delete user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting user.");
        }
    }

    public static List<Utilisateur>  afficherut(){
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Utilisateur")) {

            while (resultSet.next()) {
                int iduser = resultSet.getInt("idUtilisateur");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int roleId = resultSet.getInt("role_id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");

                Utilisateur utilisateur = new Utilisateur(iduser,username,password,roleId,nom,prenom,email);
                utilisateurs.add(utilisateur);
                /*System.out.println(utilisateur);*/


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    private static void rafraichirId() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             Statement resetStatement = connection.createStatement()) {


            ResultSet resu = resetStatement.executeQuery("SELECT MAX(idUtilisateur) FROM Utilisateur");
            int nextId = 1;
            if (resu.next()) {
                nextId = resu.getInt(1) + 1;
            }

            resetStatement.executeUpdate("ALTER TABLE Utilisateur AUTO_INCREMENT = " + nextId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error");
        }
    }

    public static void modifierUtilisateur(String username, String newUsername, String newPassword,int newRoleId,String newNom, String newPrenom, String newEmail) {
        /*/scanner  pour  essay :
        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter new username:");
        String newUsername = scanner.nextLine();

        System.out.println("Enter new password:");
        String newPassword = scanner.nextLine();

        System.out.println("Enter new role ID:");
        int newRoleId = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter new nom:");
        String newNom = scanner.nextLine();

        System.out.println("Enter new prenom:");
        String newPrenom = scanner.nextLine();

        System.out.println("Enter new email:");
        String newEmail = scanner.nextLine();*/

        // Update user in the database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "")) {
            String update = "UPDATE Utilisateur SET username = ?, password = ?, role_id = ?, nom = ?, prenom = ?, email = ? WHERE username = ?";
            PreparedStatement modifier = connection.prepareStatement(update);

            modifier.setString(1, newUsername);
            modifier.setString(2, newPassword);
            modifier.setInt(3, newRoleId);
            modifier.setString(4, newNom);
            modifier.setString(5, newPrenom);
            modifier.setString(6, newEmail);
            modifier.setString(7, username);

            int rowsUpdated = modifier.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("ok");
            } else {
                System.out.println("not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("error");
        }


    }

    public static Utilisateur checkCredentials(String username, String password) {

        String databaseUrl = "jdbc:mysql://localhost:3306/AppRestaurant";
        String dbUser = "root";
        String dbPassword = "";

        String query = "SELECT idUtilisateur, role_id FROM Utilisateur WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(databaseUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                int userId = resultSet.getInt("idUtilisateur");
                int roleId = resultSet.getInt("role_id");
                System.out.println(userId);
                return new Utilisateur(userId,roleId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error");
        }


        return null;
    }


}







