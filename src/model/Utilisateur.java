package model;

public class Utilisateur {
    private    int idUser;
    private String username;
    private String password;
    private    int roleid;
    private String nom;
    private String prenom;
    private String email;

    public Utilisateur(int iduser, String username, String password, int roleid, String nom, String prenom, String email) {
        this.idUser = iduser;
        this.username = username;
        this.password = password;
        this.roleid = roleid;
        this.nom = nom;
        this.prenom =  prenom;
        this.email = email;
    }

    public Utilisateur(int idUser, int roleid) {
        this.idUser = idUser;
        this.roleid = roleid;
    }

    public Utilisateur(String username, String password, int roleid, String nom, String prenom, String email) {
        this.username = username;
        this.password = password;
        this.roleid = roleid;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public  int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  int getRoleid() {
        return roleid;
    }

    public void setRole(int roleid) {
        this.roleid = roleid;
    }

    public String getNom() {
        return nom;
    }

    public void setName(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @Override
    public String toString() {
        return "Utilisateur{" +
                ", id='" + idUser + '\'' +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + roleid + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
