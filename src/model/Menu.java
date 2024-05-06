package model;

public class Menu {
    public int idItem;
    public String itemName;
    public int itemPrice;

    public Menu(int idItem, String itemName, int itemPrice) {
        this.idItem = idItem;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
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

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}
