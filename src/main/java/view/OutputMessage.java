package view;

public enum OutputMessage {
    INPUT_PURCHASE_INFO_GUIDE_MESSAGE("구매하실 상품명과 수량을 입력해주세요. "),
    INPUT_PURCHASE_INFO_EXAMPLE_MESSAGE("(예: [사이다-2],[감자칩-1])"),
    INPUT_PROMOTION_GUIDE_MESSAGE("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? "),
    INPUT_YES_OR_NO_GUIDE_MESSAGE("(Y/N)"),
    INPUT_MEMBERSHIP_GUID_MESSAGE("멤버십 할인을 받으시겠습니까?"),
    WELCOME_MESSAGE("안녕하세요. W편의점입니다."),
    CURRENT_INVENTORY_GUIDE_MESSAGE("현재 보유하고 있는 상품입니다."),
    THANK_YOU_ANYTHING_ELSE_MESSAGE("감사합니다. 구매하고 싶은 다른 상품이 있나요? ");

    private final String message;

    OutputMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
