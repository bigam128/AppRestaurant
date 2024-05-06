package frames;

import DAO.UserDAO;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManagementUtilisateur extends JFrame {

    private JTable userTable;
    private JButton addButton;
    private JButton deleteButton;

    public ManagementUtilisateur() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User Management");
        setSize(600, 400);

        // Create table to display users
        userTable = new JTable();
        refreshUserTable(); // Populate table with user data initially

        // Create buttons
        addButton = new JButton("Add User");
        deleteButton = new JButton("Delete User");

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AjouterUtilisateur();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String username = (String) userTable.getValueAt(selectedRow, 0); // Assuming username is in the first column
                    int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirmDialog == JOptionPane.YES_OPTION) {
                        deleteUserByUsername(username);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a user to delete.");
                }
            }
        });

        // Add components to content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        refreshUserTable();
    }

    private void refreshUserTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Role ID");
        model.addColumn("Name");

        // Fetch user data from database and populate table
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Utilisateur")) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int roleId = resultSet.getInt("role_id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");



                model.addRow(new Object[]{username, password, roleId, nom,prenom,email});
            }

            userTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserByUsername(String username) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AppRestaurant", "root", "");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Utilisateur WHERE username = ?")) {
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete user.");
            }

            refreshUserTable(); // Refresh table after deletion
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting user.");
        }
    }
    private void AjouterUtilisateur() {
        // Create dialog box to add new user
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField roleIdField = new JTextField();
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role ID:"));
        panel.add(roleIdField);
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prenom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Get user input
            String username = usernameField.getText();
            String password = passwordField.getText();
            int roleId = Integer.parseInt(roleIdField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();

            // Create Utilisateur object
            Utilisateur newUser = new Utilisateur(username, password, roleId, nom, prenom, email);

            int generatedUserId = UserDAO.addUser(newUser);
            newUser.setIdUser(generatedUserId); // Set generated user ID in the Utilisateur object

            // Refresh user table
            refreshUserTable();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagementUtilisateur frame = new ManagementUtilisateur();
            frame.setVisible(true);
            frame.refreshUserTable();
        });
    }
}

