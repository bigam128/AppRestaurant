package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public boolean connection() {
        Connection connection = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            String url = "jdbc:mysql://localhost:3306/AppRestaurant";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);

            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to database.");
            e.printStackTrace();
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}
