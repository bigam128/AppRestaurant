package DAO;

import model.Commande;
import model.CommandeContenu;

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

    public static List<CommandeContenu> getAllCommandesdetails(int idcommande) throws SQLException {
        List<CommandeContenu> commandes = new ArrayList<>();
        String query = "SELECT idCommande ,idPlat, quantite FROM CommandeItems WHERE idCommande = ?";
        try (PreparedStatement stmt = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "").prepareStatement(query)){
            stmt.setInt(1,idcommande);
             ResultSet resultSet = stmt.executeQuery(); {

            while (resultSet.next()) {
                int commandeId = resultSet.getInt("idCommande");
                int platId = resultSet.getInt("idPlat");
                int qt = resultSet.getInt("quantite");

                CommandeContenu commande = new CommandeContenu(commandeId, platId, qt);
                commandes.add(commande);
                System.out.println(commandes);
            }
        }

        return commandes;
    }
    }

    public static void updateCommandeStatus(int commandeId, String newStatus) {
        String query = "UPDATE Commandes SET status = ? WHERE idCommande = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newStatus);
            statement.setInt(2, commandeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Commande> getOrdersByUserId(int userId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM Commandes WHERE idUser = ?";
        try (PreparedStatement stmt = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "").prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int commandeId = rs.getInt("idCommande");
                    userId = rs.getInt("idUser");
                    Commande.Status status = Commande.Status.valueOf(rs.getString("status"));
                    double totalPrice = rs.getDouble("totalPrix");

                    Commande commande = new Commande(commandeId, userId, status, totalPrice);
                    commandes.add(commande);
                }
            }
        }
        return commandes;
    }


}

