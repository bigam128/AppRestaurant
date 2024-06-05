package frames;


import DAO.PlatsDAO;
import model.Plats;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ManagementPlats extends JFrame {

    private static JTable platsTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton updateButton;
    private PlatsDAO platsDAO;

    public ManagementPlats() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Plats Management");
        setSize(800, 600);

        try {
            platsDAO = new PlatsDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        platsTable = new JTable();
        remplissageTableau();

        addButton = new JButton("Add Plat");
        deleteButton = new JButton("Delete Plat");
        backButton = new JButton("Back");
        updateButton = new JButton("Modify");

        addButton.addActionListener(e -> ajouterPlat());

        deleteButton.addActionListener(e -> {
            int selectedRow = platsTable.getSelectedRow();
            if (selectedRow != -1) {
                int platId = (int) platsTable.getValueAt(selectedRow, 0);
                int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this plat?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    try {
                        platsDAO.supprimerPlat(platId);
                        remplissageTableau();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "select a plat to delete.");
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = platsTable.getSelectedRow();
            if (selectedRow != -1) {
                int platId = (int) platsTable.getValueAt(selectedRow, 0);
                int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to modify this plat?", "Confirm Modification", JOptionPane.YES_NO_OPTION);
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    modifierPlat(platId);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a plat to modify.");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        contentPane.add(new JScrollPane(platsTable), BorderLayout.CENTER);

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
        model.addColumn("NomPlat");
        model.addColumn("PrixPlat");
        model.addColumn("Description");
        model.addColumn("Image URL");

        try {
            List<Plats> platsList = platsDAO.getPlats();
            for (Plats plat : platsList) {
                model.addRow(new Object[]{plat.getIdplat(), plat.getNomPlat(), plat.getPrixPlat(), plat.getDescription(), plat.getImage_url()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        platsTable.setModel(model);
    }

    private void ajouterPlat() {
        JTextField nomPlatField = new JTextField();
        JTextField prixPlatField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField imageUrlField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom Plat:"));
        panel.add(nomPlatField);
        panel.add(new JLabel("Prix Plat:"));
        panel.add(prixPlatField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Image URL:"));
        panel.add(imageUrlField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Plat",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nomPlat = nomPlatField.getText();
            double prixPlat = Double.parseDouble(prixPlatField.getText());
            String description = descriptionField.getText();
            String imageUrl = imageUrlField.getText();

            Plats newPlat = new Plats(0, nomPlat, prixPlat, description, imageUrl);

            try {
                platsDAO.addPlat(newPlat);
                remplissageTableau();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void modifierPlat(int platId) {
        try {
            Plats plat = platsDAO.getPlatById(platId);
            System.out.println(plat);

            JTextField nomPlatField = new JTextField(plat.getNomPlat());
            JTextField prixPlatField = new JTextField(String.valueOf(plat.getPrixPlat()));
            JTextField descriptionField = new JTextField(plat.getDescription());
            JTextField imageUrlField = new JTextField(plat.getImage_url());


            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Nom Plat:"));
            panel.add(nomPlatField);
            panel.add(new JLabel("Prix Plat:"));
            panel.add(prixPlatField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Image URL:"));
            panel.add(imageUrlField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Modify Plat",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newNomPlat = nomPlatField.getText();
                double newPrixPlat = Double.parseDouble(prixPlatField.getText());
                String newDescription = descriptionField.getText();
                String newImageUrl = imageUrlField.getText();

                Plats updatedPlat = new Plats(platId, newNomPlat, newPrixPlat, newDescription, newImageUrl);
                platsDAO.modifierPlat(updatedPlat);
                remplissageTableau();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagementPlats frame = new ManagementPlats();
            frame.setVisible(true);
        });
    }
}

