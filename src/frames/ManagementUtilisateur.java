package frames;

import DAO.UserDAO;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

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

        userTable = new JTable();
        remplissageTableau();

        addButton = new JButton("Add User");
        deleteButton = new JButton("Delete User");
        backButton = new JButton("Back");
        updateButton = new JButton("Modify");

        addButton.addActionListener(e -> AjouterUtilisateur());

        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) userTable.getValueAt(selectedRow, 1);
                int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    UserDAO.deleteUserByUsername(username);
                    remplissageTableau();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to delete.");
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) userTable.getValueAt(selectedRow, 1);
                int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to modify this user?", "Confirm Modification", JOptionPane.YES_NO_OPTION);
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    modifierUtilisateur();
                    remplissageTableau();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to modify.");
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




        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void remplissageTableau() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Role ID");
        model.addColumn("Nom");
        model.addColumn("Prenom");
        model.addColumn("Email");

        List<Utilisateur> utt = UserDAO.afficherut();
        for (Utilisateur utilisateur : utt){
            model.addRow(new Object[]{utilisateur.getIdUser(),utilisateur.getUsername(),utilisateur.getPassword(),utilisateur.getRoleid(),utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getEmail()});
        }
        userTable.setModel(model);
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

