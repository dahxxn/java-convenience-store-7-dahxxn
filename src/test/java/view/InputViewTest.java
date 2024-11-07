package view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import error.ExceptionMessage;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class InputViewTest {
    InputView inputView = new InputView();

    @Test
    @DisplayName("사용자에게 값을 입력받고 이를 반환하는 테스트입니다.")
    public void 사용자에게_값_입력받기_테스트() {
        String testInputString = "Hello World";

        System.setIn(new ByteArrayInputStream(testInputString.getBytes()));
        String actualReadInput = inputView.readInput("사용자에게 입력받는 테스트입니다.");

        assertEquals(testInputString, actualReadInput);
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "  hi  "})
    @DisplayName("사용자의 입력값이 빈값인지 확인하고 빈값이 아니면 앞뒤 공백이 제거된 문자열을 반환하는 테스트입니다.")
    public void 사용자의_입력값이_빈값인지_확인하기_테스트(String testInput) {
        assertDoesNotThrow(() -> inputView.validateInputNotEmpty(testInput));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("사용자의 입력값이 공백이면 에러 메시지를 출력하는 테스트입니다.")
    public void 사용자의_입력값이_빈값인지_확인하기_예외_테스트_공백일때(String testInputValue) {
        assertThatThrownBy(() -> inputView.validateInputNotEmpty(testInputValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_EMPTY.toString());
    }

    @Test
    @DisplayName("사용자의 입력값이 널값이면 에러 메시지를 출력하는 테스트입니다.")
    public void 사용자의_입력값이_빈값인지_확인하기_예외_테스트_널값일때() {
        assertThatThrownBy(() -> inputView.validateInputNotEmpty(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_EMPTY.toString());
    }

    @Test
    public void 구매할_상품_정보_입력받기_테스트() {

    }

    @Test
    public void 구매할_상품_정보_입력받기_예외_테스트_입력형식에_맞지않을때() {

    }

    @Test
    public void 구매할_상품_정보_입력받기_예외_테스트_재고에_없는_상품이_있을때() {

    }

    @Test
    public void 구매할_상품_정보_입력받기_예외_테스트_재고수량을_초과하여_입력할때() {

    }

    @Test
    public void 예_아니오_입력받기_테스트() {

    }

    @Test
    public void 예_아니오_입력받기_예외_테스트_Y_N_이외의_문자를_입력할때() {

    }
}
