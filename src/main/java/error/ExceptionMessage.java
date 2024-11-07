package error;

public enum ExceptionMessage {
    ERROR_MESSAGE_INPUT_IS_EMPTY("빈 문자열을 입력하면 안됩니다."),
    ;

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String ERROR_MESSAGE_HEAD = "[ERROR] ";
        return ERROR_MESSAGE_HEAD + message;
    }

}
