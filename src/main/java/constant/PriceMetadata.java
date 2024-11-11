package constant;

public enum PriceMetadata {
    MEMBERSHIP_DISCOUNT_PERCENTS(30),
    MAX_MEMBERSHIP_DISCOUNT_PRICE(8000),
    ;

    private int value;

    PriceMetadata(int value) {
        this.value = value;
    }
}
