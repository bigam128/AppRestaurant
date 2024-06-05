package frames;

import DAO.CommandeDAO;
import model.Commande;
import model.CommandeContenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import static DAO.CommandeDAO.getAllCommandesdetails;

public class PersoResto extends JFrame {

    private JButton managePlatsButton;
    private JButton DetailsCommandeButton;
    private JButton ChangerStatus;
    private JButton logoutButton;
    private JPanel dishesPanel;
    private JTable commandeTable;
    private JTable TablePlat;
    private JButton manageUsersButton;




    PersoResto() {
        setDefaultCloseOperation(javax.swing.
                WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Welcome personnel");
        setSize(600, 400);


        managePlatsButton = new JButton("Manage Plats");
        DetailsCommandeButton = new JButton("Details Commande");
        ChangerStatus = new JButton("Change Status");
        logoutButton = new JButton("LogOut");

        ChangerStatus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { //on clique sur
                    int selectedRow = TablePlat.getSelectedRow();
                    if (selectedRow != -1) {
                        int commandeId = (int) TablePlat.getValueAt(selectedRow, 0); // status is in the 3eme column
                        System.out.println(commandeId); //for testing
                        String[] statuses = {"ATTENTE", "EN_COURS_DE_PREPARATION", "PRETE"};


                        String newStatus = (String) JOptionPane.showInputDialog(
                                null,
                                "Select new status:",
                                "Update Status",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                statuses,
                                statuses[0]);

                        if (newStatus != null) {
                            CommandeDAO.updateCommandeStatus(commandeId, newStatus);

                            JOptionPane.showMessageDialog(null, "Status updated successfully.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a row to change the status.");
                    }
                }
            });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                LoginFrame m = new LoginFrame();
                getContentPane().removeAll();
                getContentPane().revalidate();
                getContentPane().repaint();
                getContentPane().add(m.getContentPane());
                pack();
            }

        });


        DetailsCommandeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = TablePlat.getSelectedRow();
                System.out.println(selectedRow);
                if (selectedRow != -1) {
                    int idcommande = (int) TablePlat.getValueAt(selectedRow, 0);
                    detailsCommand(idcommande);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a commande to see details.");
                }
            }
            });


        managePlatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll(); // Remove le contenu de cette frame
                getContentPane().revalidate();
                getContentPane().repaint();

                ManagementPlats managementPlats = new ManagementPlats();
                getContentPane().add(managementPlats.getContentPane());
                pack();

            }
        });










        dishesPanel = new JPanel();
        dishesPanel.setLayout(new GridLayout(3, 3));


        affichercommande();


        JPanel topPanel = new JPanel();
        topPanel.add(managePlatsButton);
        topPanel.add(DetailsCommandeButton);
        topPanel.add(ChangerStatus);
        topPanel.add(logoutButton);


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
        contentPane.add(new JScrollPane(TablePlat), BorderLayout.WEST);
        contentPane.add(dishesPanel, BorderLayout.CENTER);
    }

    private void affichercommande(){

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Command ID");
        model.addColumn("User ID");
        model.addColumn("Total Price");
        model.addColumn("Status");

        TablePlat = new JTable(model);
        List<Commande> aff = CommandeDAO.getAllCommandes();
        System.out.println(aff);
        for (Commande commande : aff) {
            model.addRow(new Object[]{commande.getIdCommande(), commande.getUserId(), commande.getTotalPrix(), commande.getStatus()});
        }

    }
    private void detailsCommand(int idCommande){



        setTitle("Commande Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);




        List<CommandeContenu> commandes = null;
        try {
            commandes = getAllCommandesdetails(idCommande);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching command details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Commande");
        tableModel.addColumn("ID Plat");
        tableModel.addColumn("Quantite");


        if (commandes != null) {
            for (CommandeContenu commande : commandes) {
                tableModel.addRow(new Object[]{commande.getIdCommande(), commande.getIdPlat(), CommandeContenu.getQuantity()});
            }
        }

        JButton backButton  = new JButton("Back");
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


        commandeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(commandeTable);


        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PersoResto frame = new PersoResto();
            frame.setVisible(true);
        });
    }
}
