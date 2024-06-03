package DAO;

import model.Commande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {


    public static List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM Commandes";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int commandeId = resultSet.getInt("idCommande");
                int userId = resultSet.getInt("idUser");
                Commande.Status status = Commande.Status.valueOf(resultSet.getString("status"));
                double totalPrice = resultSet.getDouble("totalPrix");

                Commande commande = new Commande(commandeId, userId, status, totalPrice);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    public void updateCommandeStatus(int commandeId, String newStatus) {
        String query = "UPDATE Commande SET status = ? WHERE commande_id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newStatus);
            statement.setInt(2, commandeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

