package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements ActionListener {
    JButton b1;
    JPanel newPanel;
    JLabel userLabel, passLabel;
    final JTextField textField1, textField2;

    // Constructor
    LoginFrame() {
        userLabel = new JLabel("Username");
        textField1 = new JTextField(15);

        passLabel = new JLabel("Password");
        textField2 = new JPasswordField(15);

        b1 = new JButton("SUBMIT");

        newPanel = new JPanel(new GridLayout(3, 1));
        newPanel.add(userLabel);
        newPanel.add(textField1);
        newPanel.add(passLabel);
        newPanel.add(textField2);
        newPanel.add(b1);

        add(newPanel, BorderLayout.CENTER);

        b1.addActionListener(this);
        setTitle("Login Frame");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userValue = textField1.getText(); // Get username
        String passValue = textField2.getText(); // Get password

        int roleId = checkCredentials(userValue, passValue);
        if (roleId > 0) {
            switch (roleId) {
                case 1: // Etudiant
                    ClientFrame etudiantPage = new ClientFrame();
                     etudiantPage.setVisible(true);
                    JLabel etudiantLabel = new JLabel("Welcome etudiant: " + userValue);
                    etudiantPage.getContentPane().add(etudiantLabel);
                    break;
                case 2: // Chef
                    ChefFrame chefFrame = new ChefFrame();
                    chefFrame.setVisible(true);
                    JLabel chefLabel = new JLabel("Welcome chef" + userValue);
                    chefFrame.getContentPane().add(chefLabel);
                    break;
                case 3: // perso resto
                    PersoResto persoResto = new PersoResto();
                    persoResto.setVisible(true);
                    JLabel persLabel = new JLabel("welcome perso resto " + userValue);
                    persoResto.getContentPane().add(persLabel);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid role", "Login Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } else {

            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int checkCredentials(String username, String password) {

        String databaseUrl = "jdbc:mysql://localhost:3306/AppRestaurant";
        String dbUser = "root";
        String dbPassword = "";

        String query = "SELECT role_id FROM Utilisateur WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(databaseUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getInt("role_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }


        return -1;
    }

    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
