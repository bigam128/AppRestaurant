package frames;

import DAO.UserDAO;
import model.Utilisateur;

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

        Utilisateur ut = UserDAO.checkCredentials(userValue, passValue);
        if (ut != null) {
            int  userId = ut.getIdUser();
            int roleId = ut.getRoleid();

            switch (roleId) {
                case 1: // Etudiant
                    MainMenuFrame etudiantPage = null;
                    try {
                        etudiantPage = new MainMenuFrame(userId);
                        System.out.println("id : "+ userId);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
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
                    JOptionPane.showMessageDialog(this, "role  invalid", "Login Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } else {

            JOptionPane.showMessageDialog(this, "username or password incorrect", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
