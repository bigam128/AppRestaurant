import DAO.UserDAO;
import model.Utilisateur;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        Utilisateur user = new Utilisateur(1, "bigam", "a123", 1,"adam");

        userDAO.addUser(user);
    }
}
