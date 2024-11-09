package model;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PromotionTest {
    Promotion promotion;

    @BeforeEach
    public void setUp() {
        LocalDate startAt = LocalDate.parse("2024-11-01", DateTimeFormatter.ISO_DATE);
        LocalDate endAt = LocalDate.parse("2024-11-30", DateTimeFormatter.ISO_DATE);
        promotion = new Promotion("반짝할인", 1, 1, startAt.atStartOfDay(), endAt.atStartOfDay());
    }

    @Test
    @DisplayName("현재 시간이 프로모션 기간이면 True를 반환하는 테스트입니다. ")
    public void 현재_시간이_프로모션_기간인지_확인_테스트_True일때() {
        assertNowTest(() -> {
            assertTrue(promotion.isPromotionNow());
        }, LocalDate.of(2024, 11, 10).atStartOfDay());
    }

    @Test
    @DisplayName("현재 시간이 프로모션 기간이 아니면 False를 반환하는 테스트입니다. ")
    public void 현재_시간이_프로모션_기간인지_확인_테스트_False일때() {
        assertNowTest(() -> {
            assertFalse(promotion.isPromotionNow());
        }, LocalDate.of(2024, 10, 10).atStartOfDay());
    }

    @ParameterizedTest
    @DisplayName("구매 가능한 프로모션 상품 세트 수를 확인하는 테스트입니다.")
    @ValueSource(ints = {5, 9})
    public void 구매_가능한_프로모션_상품_세트_수량_확인_테스트(int buyCount) {
        int promotionStock = 10;
        int result = promotion.checkPromotionPossible(buyCount, promotionStock);
        int expectedResult = 0;

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("구매 가능한 프로모션 상품 세트 수를 확인하는 테스트입니다.")
    public void 구매_가능한_프로모션_상품_세트_수량_확인_테스트_프로모션_재고가_부족할때() {
        int buyCount = 12;
        int promotionStock = 10;
        int result = promotion.checkPromotionPossible(buyCount, promotionStock);
        int expectedResult = -2;

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("남은 프로모션 상품 수량 계산 테스트")
    public void 남은_프로모션_상품_수량_계산_테스트() {
        int setCount = 2;
        int initialCount = 10;
        int result = promotion.calculateRemainingCount(setCount, initialCount);
        int expectedResult = 6;

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("남은 구매 상품 수량 계산 테스트")
    public void 남은_구매_상품_수량_계산_테스트() {
        int setCount = 2;
        int initialCount = 5;
        int result = promotion.calculateRemainingCount(setCount, initialCount);
        int expectedResult = 1;

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("더 많은 프로모션을 받을 수 있는지 확인하는 테스트입니다.")
    public void 프로모션_상품을_더_받을_수_있나_확인_테스트() {
        int remainBuyCount = 1;
        int promotionStock = 6;
        boolean result = promotion.checkGetMorePromotion(remainBuyCount, promotionStock);

        assertTrue(result);
    }

    @Test
    @DisplayName("구매 가능한 프로모션 상품 세트 수를 확인하는 테스트입니다.")
    public void 구매_가능한_프로모션_상품_세트_수량_확인_테스트_추가구매_없을떄() {
        int remainBuyCount = 0;
        int promotionStock = 6;
        boolean result = promotion.checkGetMorePromotion(remainBuyCount, promotionStock);

        assertFalse(result);
    }

}
