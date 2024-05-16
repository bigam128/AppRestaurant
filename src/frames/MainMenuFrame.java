package frames;

import DAO.PlatsDAO;
import model.Plats;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainMenuFrame extends JFrame {
    private JPanel dishPanel;
    private List<Plats> cart = new ArrayList<>();

    public MainMenuFrame() throws SQLException {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        dishPanel = new JPanel(new GridLayout(0, 2));
        afficherPlats();


        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        JLabel cartLabel = new JLabel("Panier:");
        cartPanel.add(cartLabel);


        JButton viewCartButton = new JButton("Regarder Panier");
        viewCartButton.addActionListener(e -> regarderPanier());
        cartPanel.add(viewCartButton);


        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(dishPanel), BorderLayout.CENTER);
        getContentPane().add(cartPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
    }

    private void afficherPlats() throws SQLException {
        PlatsDAO plat = new PlatsDAO();
        try {
            List<Plats> dishes = plat.getPlats();
            for (Plats dish : dishes) {
                JPanel panel = new JPanel(new BorderLayout());
                JLabel nameLabel = new JLabel(dish.getItemName());
                JLabel priceLabel = new JLabel(String.format("$%.2f", dish.getItemPrice()));
                JLabel imageLabel = new JLabel();
                try {
                    BufferedImage img = ImageIO.read(new File(dish.getImage_url()));
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                } catch (IOException e) {
                    imageLabel.setText("pas de photo");
                }

                JButton addButton = new JButton("Ajouter Panier");
                addButton.addActionListener(e -> addPanier(dish));

                panel.add(nameLabel, BorderLayout.NORTH);
                panel.add(imageLabel, BorderLayout.CENTER);
                panel.add(priceLabel, BorderLayout.SOUTH);
                panel.add(addButton, BorderLayout.EAST);
                dishPanel.add(panel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + e.getMessage());
        }
    }

    private void addPanier(Plats plat) {
        cart.add(plat);
        JOptionPane.showMessageDialog(this, plat.getItemName() + " ajouter !");
    }

    private void regarderPanier() {
        StringBuilder contenu = new StringBuilder("Contenu:\n");
        for (Plats dish : cart) {
            contenu.append(dish.getItemName()).append(" - ").append(dish.getItemPrice()).append("\n");
        }
        JOptionPane.showMessageDialog(this, contenu.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainMenuFrame().setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
