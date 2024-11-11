package model;

public class Product {
    private final String name;
    private int quantity;
    private final int price;
    private final String promotion;
    static final String NULL_PROMOTION = "null";

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

    public String getPromotion() {
        return this.promotion;
    }

    public String getName() {
        return this.name;
    }

    public String formatQuantityMessage() {
        return (this.quantity == 0) ? " 재고 없음" : " " + this.quantity + "개";
    }

    public String formatPromotionMessage() {
        return (!NULL_PROMOTION.equals(this.promotion)) ? " " + this.promotion : "";
    }

    public String makeProductStockInfoMessage() {
        return new StringBuilder()
                .append("- ")
                .append(this.name)
                .append(" ")
                .append(String.format("%,d", this.price))
                .append("원")
                .append(formatQuantityMessage())
                .append(formatPromotionMessage())
                .toString();
    }
}
