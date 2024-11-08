package model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import error.ExceptionMessage;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PromotionInventoryTest {

    private PromotionInventory promotionInventory;

    @BeforeEach
    public void setUp() {
        promotionInventory = new PromotionInventory();
    }

    @Test
    @DisplayName("프로모션 인벤토리에 새로운 프로모션 정보를 추가하는 테스트입니다. ")
    public void 프로모션_정보_추가_테스트() {
        String promotionName = "그냥 2+1 행사";
        int buyCount = 2;
        int getCount = 1;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 30, 0, 0);

        promotionInventory.addPromotion(promotionName, buyCount, getCount, startAt, endAt);

        assertTrue(promotionInventory.hasPromotion(promotionName));
    }

    @Test
    @DisplayName("프로모션 인벤토리에 중복된 프로모션 정보를 추가할 경우 예외를 발생시키는 테스트입니다. ")
    public void 프로모션_추가_예외_테스트() {
        String promotionName = "그냥 2+1 행사";
        int buyCount = 2;
        int getCount = 1;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 30, 0, 0);

        promotionInventory.addPromotion(promotionName, buyCount, getCount, startAt, endAt);

        assertThatThrownBy(() -> promotionInventory.addPromotion(promotionName, buyCount, getCount, startAt, endAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_ALREADY_HAVE_PROMOTION.toString());
    }

    @Test
    @DisplayName("프로모션 이름으로 해당 프로모션의 존재 유무를 반환하는 테스트입니다. ")
    public void 프로모션_존재_유무_반환_테스트() {
        String promotionName = "그냥 2+1 행사";
        int buyCount = 2;
        int getCount = 1;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 30, 0, 0);

        promotionInventory.addPromotion(promotionName, buyCount, getCount, startAt, endAt);

        assertDoesNotThrow(() -> promotionInventory.findPromotion(promotionName));
    }

    @Test
    @DisplayName("프로모션 이름으로 해당 프로모션의 존재 유무를 반환하는 테스트입니다. ")
    public void 프로모션_존재_유무_반환_예외_테스트() {
        String testPromotionName = "없는 프로모션";

        assertThatThrownBy(() -> promotionInventory.findPromotion(testPromotionName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_CANNOT_FIND_PROMOTION.toString());
    }

}
