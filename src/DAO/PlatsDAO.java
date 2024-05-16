package DAO;

import model.Plats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatsDAO {
    private Connection connection;

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
}
