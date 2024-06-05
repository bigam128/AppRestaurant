package frames;

import DAO.CommandeDAO;
import DAO.PlatsDAO;
import DAO.PanierDAO;
import DAO.UserDAO;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainMenuFrame extends JFrame {
    private final Utilisateur utilisateur;
    private JPanel dishPanel;
    private JTable ingTable;

    private PanierDAO panierDAO;
    private int userId;

    public MainMenuFrame(int userId) throws SQLException {
        this.userId = userId;
        this.panierDAO = new PanierDAO();
        this.utilisateur = UserDAO.getUserById(userId);

        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        dishPanel = new JPanel(new GridLayout(0, 2));
        afficherPlats();

        JScrollPane scrollPane = new JScrollPane(dishPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("LogOut");
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



        JButton cartButton = new JButton("");
        try {
            BufferedImage penIcon = ImageIO.read(new File("/Users/mac/Downloads/panier.png"));
            cartButton.setIcon(new ImageIcon(penIcon.getScaledInstance(50, 25, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCart(utilisateur.getIdUser());
            }
        });


        JButton editProfileButton = new JButton("Edit Profile");
        try {
            BufferedImage penIcon = ImageIO.read(new File("/Users/mac/Desktop/pencile.png"));
            editProfileButton.setIcon(new ImageIcon(penIcon.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierUtilisateur();
            }
        });

        JButton VoirCommandeButton = new JButton("Voir Commandes");
        VoirCommandeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viewOrders();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(cartButton, BorderLayout.EAST);
        topPanel.add(logoutButton, BorderLayout.SOUTH);
        topPanel.add(VoirCommandeButton, BorderLayout.CENTER);
        topPanel.add(editProfileButton, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }
    private void afficherPlats() throws SQLException {
        PlatsDAO platDAO = new PlatsDAO();
        try {
            List<Plats> dishes = platDAO.getPlats();
            for (Plats dish : dishes) {
                JPanel panel = new JPanel(new BorderLayout());
                JLabel nameLabel = new JLabel(dish.getNomPlat(), SwingConstants.CENTER);
                JLabel priceLabel = new JLabel(String.format("$%.2f", dish.getPrixPlat()), SwingConstants.CENTER);
                JLabel imageLabel = new JLabel();

                try {
                    BufferedImage img = ImageIO.read(new File(dish.getImage_url()));
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                } catch (IOException e) {
                    imageLabel.setText("Image not found");
                }

                JButton addButton = new JButton("Add to Cart");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addToCart(dish);
                    }
                });

                JButton ModifierButton = new JButton("Modifier Ingredients");
                ModifierButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            modifierIngredients(dish.getIdplat());
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });



                JPanel gridPanel = new JPanel(new GridLayout(1, 2));
                gridPanel.add(addButton);
                gridPanel.add(ModifierButton);

                JPanel gridPanel2 = new JPanel(new GridLayout(1, 2));
                gridPanel2.add(nameLabel);
                gridPanel2.add(priceLabel);


                panel.add(gridPanel2, BorderLayout.NORTH);
                panel.add(imageLabel, BorderLayout.CENTER);

                panel.add(gridPanel, BorderLayout.SOUTH);
                dishPanel.add(panel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading dishes: " + e.getMessage());
        }
    }











    private void affichercommande(int idPlat) throws SQLException {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Plat id");
        model.addColumn("idIngredient");
        model.addColumn("NomIngredient");
        model.addColumn("quantity");

        ingTable = new JTable(model);
        List<PlatContenu> aff = PlatsDAO.getPlatsavecIngredients(idPlat);

        System.out.println("Data fetched for table: " + aff); // Debug

        for (PlatContenu plat : aff) {
            System.out.println("Adding to table: " + plat); // Debug
            model.addRow(new Object[]{plat.getIdPlat(), plat.getIdIngredient(), plat.getNomIngredient(), plat.getQuantity()});
        }

    }
    private void viewOrders() throws SQLException {
        List<Commande> commandes = CommandeDAO.getOrdersByUserId(userId);
        if (commandes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders found for this user.");
        } else {

            JFrame ordersFrame = new JFrame("Your Orders");
            ordersFrame.setSize(600, 400);
            ordersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Order ID");
            tableModel.addColumn("User ID");
            tableModel.addColumn("Total Price");
            tableModel.addColumn("Status");


            for (Commande commande : commandes) {
                tableModel.addRow(new Object[]{
                        commande.getIdCommande(),
                        commande.getUserId(),
                        commande.getTotalPrix(),
                        commande.getStatus()
                });
            }


            JTable ordersTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(ordersTable);
            ordersFrame.add(scrollPane, BorderLayout.CENTER);

            ordersFrame.setLocationRelativeTo(this);
            ordersFrame.setVisible(true);
        }
    }
    private void modifierIngredients(int idPlat) throws SQLException {

        List<PlatContenu> currentIngredients = PlatsDAO.getPlatsavecIngredients(idPlat);
        StringBuilder ingredientList = new StringBuilder();
        for (PlatContenu ingredient : currentIngredients) {
            ingredientList.append(ingredient.getNomIngredient()).append(" (").append(ingredient.getQuantity()).append(")\n");
        }


        Ingredient allIngredients = PlatsDAO.getAllIngredients();
        System.out.println(allIngredients);
        /*Ingredient selectedIngredient = (Ingredient) JOptionPane.showInputDialog(
                this,
                "Current Ingredients:\n" + ingredientList.toString() + "\n\nSelect new ingredient:",
                "Modify Ingredients",
                JOptionPane.PLAIN_MESSAGE,
                null,
                allIngredients,
                null
        );

        if (selectedIngredient != null) {

            JComboBox<PlatContenu.Quantity> quantityComboBox = new JComboBox<>(PlatContenu.Quantity.values());
            int result = JOptionPane.showConfirmDialog(
                    this,
                    quantityComboBox,
                    "Select quantity:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                PlatContenu.Quantity selectedQuantity = (PlatContenu.Quantity) quantityComboBox.getSelectedItem();

                PlatsDAO platDAO = new PlatsDAO();
                platDAO.addIngredientToPlat(idPlat, selectedIngredient.getIdIngredient(), selectedIngredient.getNomIngredient(), selectedQuantity);

                JOptionPane.showMessageDialog(this, "Ingredient added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No quantity selected.");
            }
        }*/
    }



    private void addToCart(Plats plat) {
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:", "Add to Cart", JOptionPane.PLAIN_MESSAGE);
        if (quantityStr != null) {
            try {
                int quantite = Integer.parseInt(quantityStr);
                System.out.println("panier id : " +userId);
                panierDAO.addPlat(plat, quantite,userId);
                JOptionPane.showMessageDialog(this, "Added to cart!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity entered.");
            }
        }
    }


    private void viewCart(int userId) {
        PanierDAO panierDAO = new PanierDAO();
        List<Panier> panierItems = panierDAO.getPanierByUserId(userId);

        StringBuilder cartDetails = new StringBuilder();
        double totalPrice = 0;
        for (Panier item : panierItems) {
            cartDetails.append("Plat ID: ").append(item.getIdPlat())
                    .append(", Quantity: ").append(item.getQuantity())
                    .append(", Total Price: $").append(item.getTotalPrix()).append("\n");
            totalPrice += item.getTotalPrix();
        }
        System.out.println(panierItems);

        int confirmation = JOptionPane.showConfirmDialog(this, "Panier:\n" + cartDetails + "\nTotal Prix: $" + totalPrice + "\nDo you want to validate?", "Cart", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            List<Commande> commandes = panierDAO.validate(userId);
            JOptionPane.showMessageDialog(this, "Order placed successfully! Total: $" + totalPrice);
        } else {
            String[] options = {"Modify Quantity", "Remove Item", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this, "Would you like to modify the cart?", "Modify Cart",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {

                JComboBox<Integer> platIdComboBox = new JComboBox<>();
                for (Panier item : panierItems) {
                    platIdComboBox.addItem(item.getIdPlat());
                }

                JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("Select Plat ID:"));
                panel.add(platIdComboBox);
                panel.add(new JLabel("Enter new quantity:"));
                JTextField quantityField = new JTextField();
                panel.add(quantityField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Modify Quantity", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int platId = (int) platIdComboBox.getSelectedItem();
                    int newQuantity = Integer.parseInt(quantityField.getText());
                    panierDAO.modifierPanier(userId, platId, newQuantity);
                    JOptionPane.showMessageDialog(this, "Quantity updated successfully.");
                }
            } else if (choice == 1) {

                JComboBox<Integer> platIdComboBox = new JComboBox<>();
                for (Panier item : panierItems) {
                    platIdComboBox.addItem(item.getIdPlat());
                }

                JPanel panel = new JPanel(new GridLayout(1, 2));
                panel.add(new JLabel("Select Plat ID to remove:"));
                panel.add(platIdComboBox);

                int result = JOptionPane.showConfirmDialog(this, panel, "Remove Item", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int platId = (int) platIdComboBox.getSelectedItem();
                    panierDAO.removePlat(platId);
                    JOptionPane.showMessageDialog(this, "Item removed successfully.");
                }

            }

        }
    }



    private void modifierUtilisateur() {


        //on get  les valeuurs du tab
        String username =  utilisateur.getUsername();
        String password = utilisateur.getPassword();
        int roleId = utilisateur.getRoleid();
        String nom = utilisateur.getNom();
        String prenom = utilisateur.getPrenom();
        String email = utilisateur.getEmail();

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

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainMenuFrame(1).setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

