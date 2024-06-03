package DAO;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierDAO {

    public void addPlat(Plats plat, int quantity, int userId) {
        int autoIdp = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "") ){
            String sql = "INSERT INTO Panier (plat_id, quantity, id_User, prix_total) VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, plat.getIdItem());
            statement.setInt(2, quantity);
            statement.setInt(3, userId);
            statement.setDouble(4, plat.getItemPrice());
            statement.executeUpdate();
            ResultSet rss = statement.getGeneratedKeys();
            if (rss.next()) {
                autoIdp = rss.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePlat(int platId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "")) {
            String sql = "DELETE FROM Panier WHERE plat_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, platId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getTotalPrice() {
        double total = 0.0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "")) {
            String sql = "SELECT P.prix, C.quantity FROM Panier C JOIN Plats P ON C.plat_id = P.idPlat";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                total += resultSet.getDouble("prix") * resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<Commande> validate(int userId) {
        List<Commande> commandes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "")) {
            connection.setAutoCommit(false);

            // Create a new order
            String sqlCommande = "INSERT INTO Commandes (idUser, status, totalPrix) VALUES (?, ?, ?)";
            PreparedStatement statementCommande = connection.prepareStatement(sqlCommande, PreparedStatement.RETURN_GENERATED_KEYS);
            statementCommande.setInt(1, userId);
            statementCommande.setString(2, "ATTENTE");
            statementCommande.setDouble(3, getTotalPrice());
            statementCommande.executeUpdate();

            ResultSet rs = statementCommande.getGeneratedKeys();
            if (rs.next()) {
                int commandeId = rs.getInt(1);


                String sqlItems = "SELECT plat_id, quantity FROM Panier WHERE id_User = ?";
                PreparedStatement statementSelectItems = connection.prepareStatement(sqlItems);
                statementSelectItems.setInt(1, userId);
                ResultSet resultSet = statementSelectItems.executeQuery();

                List<CommandeContenu> commandeContenuList = new ArrayList<>();

                while (resultSet.next()) {
                    int platId = resultSet.getInt("plat_id");
                    int quantity = resultSet.getInt("quantity");

                    String sqlInsertItems = "INSERT INTO CommandeItems (idCommande, idPlat, quantite) VALUES (?, ?, ?)";
                    PreparedStatement statementInsertItems = connection.prepareStatement(sqlInsertItems, PreparedStatement.RETURN_GENERATED_KEYS);
                    statementInsertItems.setInt(1, commandeId);
                    statementInsertItems.setInt(2, platId);
                    statementInsertItems.setInt(3, quantity);
                    statementInsertItems.executeUpdate();

                    ResultSet rsItems = statementInsertItems.getGeneratedKeys();
                    if (rsItems.next()) {
                        int commandeItemId = rsItems.getInt(1);
                        CommandeContenu commandeItem = new CommandeContenu(commandeItemId, commandeId, platId, quantity);
                        commandeContenuList.add(commandeItem);
                    }
                }

                String sqlClear = "DELETE FROM Panier WHERE id_User = ?";
                PreparedStatement statementClear = connection.prepareStatement(sqlClear);
                statementClear.setInt(1, userId);
                statementClear.executeUpdate();
                connection.commit();
                rafraichirIdp();
                Commande commande = new Commande(commandeId, userId, Commande.Status.ATTENTE, getTotalPrice());
                commande.setPs(commandeContenuList);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    private static void rafraichirIdp() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             Statement resetStatement = connection.createStatement()) {


            ResultSet resu = resetStatement.executeQuery("SELECT MAX(plat_id) FROM Panier");
            int nextId = 1;
            if (resu.next()) {
                nextId = resu.getInt(1) + 1;
            }

            resetStatement.executeUpdate("ALTER TABLE Panier AUTO_INCREMENT = " + nextId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error");
        }
    }
}
