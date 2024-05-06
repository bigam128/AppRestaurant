package model;

public class Commande {
    private int idCommande;
    private int userId;
    private int itemId;
    public int quantity;
    public Status status; // Enum variable


    // Enum definition
    public enum Status {
        PENDING,
        IN_PROGRESS,
        READY,
        DELIVERED
    }

    public Commande(int idCommande, int userId, int itemId, int quantity, Status status) {
        this.idCommande = idCommande;
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.status = status;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
