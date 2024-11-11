package constant;

public enum FileMetadata {
    PRODUCT_MD_FILE_PATH("src/main/resources/products.md"),
    PROMOTION_MD_FILE_PATH("src/main/resources/promotions.md"),
    DELIMITER(","),
    NULL_PROMOTION("null");

    private String value;

    FileMetadata(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }


}
