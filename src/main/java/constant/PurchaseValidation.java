package constant;

public enum PurchaseValidation {
    SINGLE_PURCHASE_INFO_REGEX("^\\[([a-zA-Z가-힣]+)-([1-9][0-9]*)\\]$"),
    MULTIPLE_PURCHASE_INFO_REGEX("^\\[([a-zA-Z가-힣]+)-([1-9][0-9]*)\\](,\\[([a-zA-Z가-힣]+)-([1-9][0-9]*)\\])*$"),
    YES("Y"),
    NO("N");

    private final String value;

    PurchaseValidation(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
