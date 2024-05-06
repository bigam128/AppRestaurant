package frames;

import DAO.UserDAO;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PersoResto extends JFrame {

    private JButton profileButton;
    private JButton settingsButton;
    private JPanel dishesPanel;
    private JTable mealTable;
    private JButton manageUsersButton;  // Button for managing user

    PersoResto() {
        setDefaultCloseOperation(javax.swing.
                WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Welcome personnel");
        setSize(600, 400);

        // Create buttons for profile and settings
        profileButton = new JButton("Profile");
        settingsButton = new JButton("Settings");

        // Create panel for dishes
        dishesPanel = new JPanel();
        dishesPanel.setLayout(new GridLayout(3, 3)); // Example grid layout, adjust as needed

        // Create table for meals in preparation
        String[] columnNames = {"Meal", "Status"};
        Object[][] data = {
                {"Spaghetti", "Preparing"},
                {"Salad", "Waiting"},
                {"Steak", "Preparing"}
                // Add more rows as needed
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        mealTable = new JTable(model);

        // Add buttons to top of frame
        JPanel topPanel = new JPanel();
        topPanel.add(profileButton);
        topPanel.add(settingsButton);

        // Add button for managing users
        manageUsersButton = new JButton("Manage Users");
        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open UserManagementFrame when button is clicked
                ManagementUtilisateur userManagementFrame = new ManagementUtilisateur();
                userManagementFrame.setVisible(true);
            }
        });
        topPanel.add(manageUsersButton);


        // Add components to content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(mealTable), BorderLayout.WEST);
        contentPane.add(dishesPanel, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PersoResto frame = new PersoResto();
            frame.setVisible(true);
        });
    }
}
