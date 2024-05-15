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

    private static JTable userTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton updateButton;

    public ManagementUtilisateur() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User Management");
        setSize(600, 400);

        // Create table
        userTable = new JTable();
        remplissageTableau();

        // Create buttons
        addButton = new JButton("Add User");
        deleteButton = new JButton("Delete User");
        backButton = new JButton("back");
        updateButton = new JButton("Modify");

        // Adding action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AjouterUtilisateur();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //on clique sur
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String username = (String) userTable.getValueAt(selectedRow, 1); // username is in the deuxieme column
                    System.out.println(username); //for testing
                    int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirmDialog == JOptionPane.YES_OPTION) {
                        UserDAO.deleteUserByUsername(username);
                        remplissageTableau();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a user to delete.");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                    PersoResto m = new PersoResto();
                    getContentPane().removeAll();
                    getContentPane().revalidate();
                    getContentPane().repaint();
                    getContentPane().add(m.getContentPane());
                    pack();
                }

        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierUtilisateur();
            }

        });



        // ajouter a la pane le contenu
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        remplissageTableau();
    }


    //cette methode a pour role de rafraichir le contenu du tableau en cas d'ajout ou de supression
    private void remplissageTableau() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Role ID");
        model.addColumn("Nom");
        model.addColumn("Prenom");
        model.addColumn("Email");


        //
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



                model.addRow(new Object[]{iduser,username, password, roleId, nom,prenom,email});
            }

            userTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void AjouterUtilisateur() {
        // Adding user
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

            int idut = UserDAO.addUser(newUser); // Adding user to db
            newUser.setIdUser(idut); // Set  ID in the Utilisateur object

            // Remplir user table
            remplissageTableau();
        }
    }

    private void modifierUtilisateur() {
        // on selectionne la column qu'on veut
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "select a user to modify.");
            return;
        }

        //on get  les valeuurs du tab
        String username = (String) userTable.getValueAt(selectedRow, 1);
        String password = (String) userTable.getValueAt(selectedRow, 2);
        int roleId = (int) userTable.getValueAt(selectedRow, 3);
        String nom = (String) userTable.getValueAt(selectedRow, 4);
        String prenom = (String) userTable.getValueAt(selectedRow, 5);
        String email = (String) userTable.getValueAt(selectedRow, 6);

        // on cree ca pour modifier
        JTextField usernameField = new JTextField(username);
        JTextField passwordField = new JTextField(password);
        JTextField roleIdField = new JTextField(String.valueOf(roleId));
        JTextField nomField = new JTextField(nom);
        JTextField prenomField = new JTextField(prenom);
        JTextField emailField = new JTextField(email);

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

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // on get le input
            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();
            int newRoleId = Integer.parseInt(roleIdField.getText());
            String newNom = nomField.getText();
            String newPrenom = prenomField.getText();
            String newEmail = emailField.getText();

            // on fait la maj (maj = mise a jour)
            UserDAO.modifierUtilisateur(username,newUsername, newPassword,newRoleId, newNom, newPrenom, newEmail);

            // on remplie le tableau encore une autre fois
            remplissageTableau();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagementUtilisateur frame = new ManagementUtilisateur();
            frame.setVisible(true);
            frame.remplissageTableau();
        });
    }
}

