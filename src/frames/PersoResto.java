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


        profileButton = new JButton("Profile");
        settingsButton = new JButton("Settings");


        dishesPanel = new JPanel();
        dishesPanel.setLayout(new GridLayout(3, 3));


        String[] columnNames = {"Meal", "Status"};
        Object[][] data = {
                {"Spaghetti", "Preparing"},
                {"Salad", "Waiting"},
                {"Steak", "Preparing"}

        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        mealTable = new JTable(model);


        JPanel topPanel = new JPanel();
        topPanel.add(profileButton);
        topPanel.add(settingsButton);


        manageUsersButton = new JButton("Manage Users");
        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll(); // Remove le contenu de cette frame
                getContentPane().revalidate();
                getContentPane().repaint();

                ManagementUtilisateur managementUtilisateur = new ManagementUtilisateur();
                getContentPane().add(managementUtilisateur.getContentPane()); // Ajouter le contenu de ManagementUtilisateur  frame
                pack();
            }
        });
        topPanel.add(manageUsersButton);



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
