package model;

public class Plats {
    public int idplat;
    public String nomPlat;
    public double prixPlat;
    public String  description;

    public String image_url;

    public Plats(int idplat, String nomPlat, int prixPlat, String image_url) {
        this.idplat = idplat;
        this.nomPlat = nomPlat;
        this.prixPlat = prixPlat;
        this.image_url = image_url;
    }

    public Plats(int idplat, String nomPlat, double prixPlat, String description, String imageUrl) {
        this.idplat = idplat;
        this.nomPlat = nomPlat;
        this.prixPlat = prixPlat;
        this.description = description;
        this.image_url = imageUrl;
    }

    public int getIdplat() {
        return idplat;
    }

    public void setIdplat(int idplat) {
        this.idplat = idplat;
    }

    public String getNomPlat() {
        return nomPlat;
    }

    public void setNomPlat(String nomPlat) {
        this.nomPlat = nomPlat;
    }

    public double getPrixPlat() {
        return prixPlat;
    }

    public void setPrixPlat(int prixPlat) {
        this.prixPlat = prixPlat;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void addIngredient(Ingredient ingredient) {

    }

    public void setPrixPlat(double prixPlat) {
        this.prixPlat = prixPlat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Plats{" +
                "description='" + description + '\'' +
                '}';
    }
}
