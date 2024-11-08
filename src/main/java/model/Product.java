package model;

public class Product {
    private final String name;
    private int quantity;
    private final int price;
    private final String promotion;


    public Product(String name, int quantity, int price, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void addStock(int value) {
        this.quantity += value;
    }

    public void subStock(int value) {
        this.quantity -= value;
    }

    public int getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
