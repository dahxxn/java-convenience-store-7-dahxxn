package model;

public enum ProductFileStructure {
    PRODUCT_NAME(1),
    PRODUCT_PRICE(2),
    PRODUCT_QUANTITIES(3),
    PRODUCT_PROMOTION(4);

    private final int index;

    ProductFileStructure(int index) {
        this.index = index;
    }
}
