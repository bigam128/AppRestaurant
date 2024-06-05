package model;

import java.util.List;

public class Commande {
    private int idCommande;
    private int userId;

    private Status status;// Enum variable

    private double totalPrix;
    private List<CommandeContenu> ps;


    // Enum definition
    public enum Status {
        ATTENTE,
        EN_COURS_DE_PREPARATION,
        PRETE
    }

    public Commande(int idCommande, int userId, Status status,double totalPrix) {
        this.idCommande = idCommande;
        this.userId = userId;
        this.status = status;
        this.totalPrix = totalPrix;
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


    public Status getStatus() {
        return status;
    }

    public Status setStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getTotalPrix() {
        return totalPrix;
    }

    public List<CommandeContenu> getPs() {
        return ps;
    }

    public void setPs(List<CommandeContenu> ps) {
        this.ps = ps;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + idCommande +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrix +
                '}';
    }
}
