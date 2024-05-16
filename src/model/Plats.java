package model;

public class Plats {
    public int idItem;
    public String itemName;
    public double itemPrice;
    public String image_url;

    public Plats(int idItem, String itemName, int itemPrice, String image_url) {
        this.idItem = idItem;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.image_url = image_url;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
