package model;

public enum PromotionFileStructure {
    PROMOTION_NAME(1),
    PROMOTION_BUY_COUNT(2),
    PROMOTION_GET_COUNT(3),
    PROMOTION_START_AT(4),
    PROMOTION_END_AT(5);

    private final int index;

    PromotionFileStructure(int index) {
        this.index = index;
    }
}
