package model;

public enum ProductInfoLineStructure {
    PRODUCT_NAME(1),
    PRODUCT_PRICE(2),
    PRODUCT_QUANTITIES(3),
    PRODUCT_PROMOTION(4);

    private final int index;

    ProductInfoLineStructure(int index) {
        this.index = index;
    }
}
