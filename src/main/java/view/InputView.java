package view;

import camp.nextstep.edu.missionutils.Console;
import error.CustomException;
import error.ExceptionMessage;

public class InputView {
    OutputView outputView = new OutputView();

    public String readInput(String message) {
        outputView.printMessage(message);
        return Console.readLine();
    }

    public String validateInputNotEmpty(String inputValue) {
        if (inputValue == null || inputValue.trim().isEmpty()) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_EMPTY);
        }
        return inputValue;
    }


}
