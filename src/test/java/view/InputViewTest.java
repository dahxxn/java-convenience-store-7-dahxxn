package view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import error.ExceptionMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.ProductInventory;
import model.PromotionInventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class InputViewTest {
    PromotionInventory promotionInventory = new PromotionInventory();
    ProductInventory productInventory = new ProductInventory(promotionInventory);
    InputView inputView = new InputView(productInventory);

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
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_WRONG.toString())
        ;
    }

    @Test
    @DisplayName("사용자의 입력값이 널값이면 에러 메시지를 출력하는 테스트입니다.")
    public void 사용자의_입력값이_빈값인지_확인하기_예외_테스트_널값일때() {
        assertThatThrownBy(() -> inputView.validateInputNotEmpty(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_WRONG.toString())
        ;
    }

    @ParameterizedTest
    @DisplayName("상품 정보 입력 문자열이 [상품이름-갯수] 혹은 [상품이름-갯수],[상품이름-갯수], ... 형식으로 되어있는지 확인하는 테스트입니다. ")
    @ValueSource(strings = {"[콜라-1]", "[콜라-10],[물-2]", "[콜라-1],[물-2],[사이다-3]"})
    public void 상품_정보_입력_형식_검증_테스트(String testInputPurchaseInfo) {
        assertDoesNotThrow(() -> inputView.validateInputPurchaseInfoFormat(testInputPurchaseInfo));
    }

    @ParameterizedTest
    @DisplayName("상품 정보 입력 시, 상품 이름에 숫자와 특수기호가 오는 경우 예외를 던지는 테스트입니다. ")
    @ValueSource(strings = {"[123-1]", "[콜라-1],[123-1]", "[!12-1]", "[콜라12-1]"})
    public void 상품_정보_입력_형식_검증_예외_테스트_상품이름에_숫자나_특수기호가_올때(String testInputPurchaseInfo) {
        assertThatThrownBy(() -> inputView.validateInputPurchaseInfoFormat(testInputPurchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_NOT_VALID.toString());
    }

    @ParameterizedTest
    @DisplayName("상품 정보 입력 시, 구매 갯수에 숫자형식이 아닌 값이 있는 경우 예외를 던지는 테스트입니다. ")
    @ValueSource(strings = {"[콜라-콜라]", "[콜라-1],[콜라-콜라]", "[콜라-!!]", "[콜라-]", "[콜라-010]"})
    public void 상품_정보_입력_형식_검증_예외_테스트_구매갯수에_숫자형식이_아닌_값이_올때(String testInputPurchaseInfo) {
        assertThatThrownBy(() -> inputView.validateInputPurchaseInfoFormat(testInputPurchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_NOT_VALID.toString());
    }

    @Test
    @DisplayName("상품 정보를 여러 개 입력시, 쉼표(,) 뒤에 추가로 상품 정보를 적지 않은 경우 예외를 던지는 테스트입니다. ")
    public void 상품_정보_입력_형식_검증_예외_테스트_쉼표_뒤가_비어있을_때() {
        String testInputPurchaseInfo = "[콜라-10],";
        assertThatThrownBy(() -> inputView.validateInputPurchaseInfoFormat(testInputPurchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_NOT_VALID.toString());
    }

    @Test
    @DisplayName("하나의 상품 정보에 대해서 이름을 추출하는 테스트입니다. ")
    public void 상품_정보에서_상품_이름_추출_테스트() {
        String testInputPurchase = "[콜라-1]";
        String expectResult = "콜라";
        String actualResult = inputView.extractPurchaseName(testInputPurchase);

        assertEquals(expectResult, actualResult);
    }

    @Test
    @DisplayName("하나의 상품 정보에 대해서 상품 갯수를 추출하는 테스트입니다. ")
    public void 상품_정보에서_상품_갯수_추출_테스트() {
        String testInputPurchase = "[콜라-10]";
        int expectResult = 10;
        int actualResult = inputView.extractQuantity(testInputPurchase);

        assertEquals(expectResult, actualResult);
    }

    @Test
    @DisplayName("문자열을 구분자인 쉼표(,)를 기준으로 자르는 테스트입니다. ")
    public void 문자열을_구분자인_쉼표로_자르는_테스트() {
        String testInputPurchaseInfo = "[콜라-1],[물-2]";
        List<String> expectedResult = new ArrayList<>(List.of("[콜라-1]", "[물-2]"));
        List<String> actualResult = inputView.delimiterInputPurchaseInfo(testInputPurchaseInfo);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("사용자가 입력한 상품 정보로부터 (상품명,구매갯수)로 구성된 맵 객체를 반환하는 테스트입니다. ")
    public void 구매할_상품_정보로_맵반환_테스트() {
        String testInputPurchaseInfo = "[콜라-1],[물-2]";
        HashMap<String, Integer> expectedResult = new HashMap<>();
        expectedResult.put("콜라", 1);
        expectedResult.put("물", 2);

        HashMap<String, Integer> actualResult = inputView.extractPurchaseInfoToMap(testInputPurchaseInfo);

        assertEquals(expectedResult, actualResult);
    }


    @ParameterizedTest
    @ValueSource(strings = {"Y", "N"})
    @DisplayName("사용자에게 받은 입력값이 Y혹은 N인지 확인하는 테스트입니다.")
    public void Y_혹은_N으로_입력받기_테스트(String testInputYesOrNoInfo) {
        assertDoesNotThrow(() -> inputView.validateInputYesOrNoInfo(testInputYesOrNoInfo));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yeah", "NOPE"})
    @DisplayName("사용자에게 받은 입력값이 Y/N 이외의 문자를 입력할때 예외를 던지는 테스트입니다. ")
    public void Y_혹은_N으로_입력받기_예외_테스트_Y_N_이외의_문자를_입력할때(String testInputYesOrNoInfo) {
        assertThatThrownBy(() -> inputView.validateInputYesOrNoInfo(testInputYesOrNoInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_WRONG.toString());
    }

}
