package dto;

import model.Product;

public class ProductInfoDto {
    String name;
    int quantity;
    int price;
    String promotion;

    public ProductInfoDto(String name, int quantity, int price, String promotion) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    public Product generateProduct() {
        return new Product(name, quantity, price, promotion);
    }

}
