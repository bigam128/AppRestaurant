package model;

public class Utilisateur {
    private int idUser;
    private String username;
    private String password;
    private int roleid;
    private String name;

    public Utilisateur(int idUser, String username, String password, int roleid, String name) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.roleid = roleid;
        this.name = name;
    }

    public int getIdUser() {
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

    public int getRoleid() {
        return roleid;
    }

    public void setRole(int roleid) {
        this.roleid = roleid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
