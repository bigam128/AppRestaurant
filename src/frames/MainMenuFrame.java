package frames;

import DAO.PlatsDAO;
import DAO.PanierDAO;
import model.Plats;
import model.Commande;
import model.Utilisateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainMenuFrame extends JFrame {
    private JPanel dishPanel;
    private PanierDAO panierDAO;
    private int userId;

    public MainMenuFrame(int userId) throws SQLException {
        this.userId = userId;
        this.panierDAO = new PanierDAO();

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

        // Add cart button
        JButton cartButton = new JButton("View Cart");
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(cartButton, BorderLayout.EAST);
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
                JLabel nameLabel = new JLabel(dish.getItemName(), SwingConstants.CENTER);
                JLabel priceLabel = new JLabel(String.format("$%.2f", dish.getItemPrice()), SwingConstants.CENTER);
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

                panel.add(nameLabel, BorderLayout.NORTH);
                panel.add(imageLabel, BorderLayout.CENTER);
                panel.add(priceLabel, BorderLayout.SOUTH);
                panel.add(addButton, BorderLayout.SOUTH);
                dishPanel.add(panel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading dishes: " + e.getMessage());
        }
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

    private void viewCart() {
        double totalPrice = panierDAO.getTotalPrice();
        int confirmation = JOptionPane.showConfirmDialog(this, "Total Price: $" + totalPrice + "\nDo you want to validate the cart?", "Cart", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            List<Commande> commandes = panierDAO.validate(userId);

            JOptionPane.showMessageDialog(this, "Order placed successfully! Total: $" + totalPrice);

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
