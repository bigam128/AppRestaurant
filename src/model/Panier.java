package model;

public class Panier {
    private int idPanier;
    private int idPlat;

    private int quantity;
    private int idUser;

    private double totalPrix;


    public Panier() {
    }


    public Panier(int idPanier, int idPlat, int quantity,int idUser, double totalPrix) {
        this.idPanier = idPanier;
        this.idPlat = idPlat;
        this.quantity = quantity;
        this.idUser = idUser;
        this.totalPrix = totalPrix;
    }


    public Panier( int idPlat, int quantity, int idUser,double totalPrix) {
        this.idPlat = idPlat;
        this.quantity = quantity;
        this.idUser = idUser;
        this.totalPrix = totalPrix;
    }

    public Panier(int idPlat, int quantity, double totalPrix) {
        this.idPlat = idPlat;
        this.quantity = quantity;
        this.totalPrix = totalPrix;
    }

    // Getters and Setters
    public int getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(int idPanier) {
        this.idPanier = idPanier;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrix() {
        return totalPrix;
    }

    public void setTotalPrix(double totalPrix) {
        this.totalPrix = totalPrix;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "idPlat=" + idPlat +
                ", quantity=" + quantity +
                ", totalPrix=" + totalPrix +
                '}';
    }
}
