package error;

public enum ExceptionMessage {
    ERROR_MESSAGE_INPUT_IS_WRONG("잘못된 입력입니다. 다시 입력해 주세요."),
    ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_NOT_VALID("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    ERROR_MESSAGE_INPUT_PURCHASE_INFO_NOT_EXIST_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_OUT_OF_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    ERROR_MESSAGE_CANNOT_READ_FILE("파일을 찾을 수 없습니다."),
    ERROR_MESSAGE_CANNOT_FIND_PROMOTION("해당 프로모션을 찾을 수 없습니다."),
    ERROR_MESSAGE_ALREADY_HAVE_PROMOTION("해당 프로모션은 이미 존재합니다."),
    ;

    private final String ERROR_MESSAGE_HEAD = "[ERROR] ";
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return this.ERROR_MESSAGE_HEAD + message;
    }

}
