package DAO;

import model.Commande;
import model.Ingredient;
import model.PlatContenu;
import model.Plats;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlatsDAO {
    private static Connection connection;

    public PlatsDAO() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
    }

    public List<Plats> getPlats() throws SQLException {
        List<Plats> dishes = new ArrayList<>();
        String query = "SELECT idPlat, NomPlat, prix, photo_url FROM Plats";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Plats plat = new Plats(
                        rs.getInt("idPlat"),
                        rs.getString("NomPlat"),
                        rs.getInt("prix"),
                        rs.getString("photo_url")
                );
                dishes.add(plat);
            }
        }
        return dishes;
    }
    public void addPlat(Plats plat) throws SQLException {
        String query = "INSERT INTO Plats (NomPlat, prix, photo_url) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, plat.getNomPlat());
            stmt.setDouble(2, plat.getPrixPlat());
            stmt.setString(3, plat.getImage_url());
            stmt.executeUpdate();
        }
    }


    public void modifierPlat(Plats plat) throws SQLException {
        String query = "UPDATE Plats SET NomPlat = ?, prix = ?, Description = ? ,photo_url = ? WHERE idPlat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, plat.getNomPlat());
            stmt.setDouble(2, plat.getPrixPlat());
            stmt.setString(3, plat.getDescription());
            stmt.setString(4, plat.getImage_url());
            stmt.setInt(5, plat.getIdplat());
            stmt.executeUpdate();
        }
    }

    public void supprimerPlat(int idPlat) throws SQLException {
        String query = "DELETE FROM Plats WHERE idPlat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPlat);
            stmt.executeUpdate();
        }
    }

    public static List<PlatContenu> getPlatsavecIngredients(int idPlat) {
        List<PlatContenu> platContenus = new ArrayList<>();
        String query = "SELECT DISTINCT idIngredient, quantity FROM PlatContenu WHERE idPlat = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPlat);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PlatContenu platContenu = new PlatContenu(
                            rs.getString("idIngredient"),
                            PlatContenu.Quantity.fromString(rs.getString("quantity"))
                    );



                    platContenus.add(platContenu);

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return platContenus;

    }


    public void updateIngredients(int platId, String nomIngredient, List<Ingredient> newIngredients) throws SQLException {
        String deleteQuery = "DELETE FROM PlatContenu WHERE idPlat = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, platId);
            deleteStmt.executeUpdate();
        }

        String insertQuery = "INSERT INTO PlatContenu (idPlat, idIngredient, NomIngredient) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            for (Ingredient ingredient : newIngredients) {
                insertStmt.setInt(1, platId);
                insertStmt.setInt(2, ingredient.getIdIngredient());
                insertStmt.setString(3, ingredient.getNomIngredient());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

    public static Ingredient getAllIngredients() {
        ArrayList<Ingredient> ingredts = new ArrayList<>();
        String query = "SELECT * FROM Ingredients";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                int iding = rs.getInt("idIngredient");
                String noming = rs.getString("NomIngredient");
                return new Ingredient(iding,noming);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addIngredientToPlat(int idPlat, int idIngredient, String nomIngredient, PlatContenu.Quantity quantity) throws SQLException {
        String query = "INSERT INTO PlatContenu (idPlat, idIngredient, NomIngredient, quantity) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPlat);
            stmt.setInt(2, idIngredient);
            stmt.setString(3, nomIngredient);
            stmt.setString(4, quantity.toString());
            stmt.executeUpdate();
        }
    }

    public Plats getPlatById(int idPlat) throws SQLException {
        String query = "SELECT * FROM Plats WHERE idPlat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPlat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Plats(
                            rs.getInt("idPlat"),
                            rs.getString("NomPlat"),
                            rs.getDouble("prix"),
                            rs.getString("description"),
                            rs.getString("photo_url")
                    );
                }
            }
        }
        return null;
    }




}



