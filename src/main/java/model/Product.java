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

    public String makeQuantityMessage() {
        if (this.quantity == 0) {
            return " 재고 없음";
        }
        return " " + this.quantity + "개";
    }

    public String makePromotionMessage() {
        if (!this.promotion.equals(NULL_PROMOTION)) {
            return " " + this.promotion;
        }
        return "";
    }

    public String getProductStockInfo() {
        String stockInfo = "- "
                + this.name + " " + String.format("%,d", this.price) + "원"
                + makeQuantityMessage()
                + makePromotionMessage();
        return stockInfo;
    }
}
