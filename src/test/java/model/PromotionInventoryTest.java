package model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dto.PromotionInfoDto;
import error.ExceptionMessage;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PromotionInventoryTest {

    private PromotionInventory promotionInventory;

    @BeforeEach
    public void setUp() {
        promotionInventory = new PromotionInventory();
    }


    @Test
    public void 프로모션_정보_추가_테스트() {
        String promotionName = "그냥 2+1 행사";
        int buyCount = 2;
        int getCount = 1;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 30, 0, 0);

        PromotionInfoDto promotionInfoDto = new PromotionInfoDto(promotionName, buyCount, getCount, startAt, endAt);
        promotionInventory.addPromotion(promotionInfoDto);

        assertTrue(promotionInventory.hasPromotion(promotionName));
    }

    @Test
    public void 프로모션_중복_추가_예외_테스트() {
        String promotionName = "그냥 2+1 행사";
        int buyCount = 2;
        int getCount = 1;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 30, 0, 0);

        PromotionInfoDto promotionInfoDto = new PromotionInfoDto(promotionName, buyCount, getCount, startAt, endAt);
        promotionInventory.addPromotion(promotionInfoDto);

        assertThatThrownBy(() -> promotionInventory.addPromotion(promotionInfoDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_ALREADY_HAVE_PROMOTION.toString());
    }

    @Test
    public void 프로모션_존재_유무_반환_테스트() {
        String promotionName = "그냥 2+1 행사";
        int buyCount = 2;
        int getCount = 1;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 30, 0, 0);

        PromotionInfoDto promotionInfoDto = new PromotionInfoDto(promotionName, buyCount, getCount, startAt, endAt);
        promotionInventory.addPromotion(promotionInfoDto);

        assertDoesNotThrow(() -> promotionInventory.findPromotion(promotionName));
    }

    @Test
    public void 프로모션_존재_유무_반환_예외_테스트() {
        String testPromotionName = "없는 프로모션";

        assertThatThrownBy(() -> promotionInventory.findPromotion(testPromotionName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_CANNOT_FIND_PROMOTION.toString());
    }

    @Test
    public void 날짜_변환_테스트() {
        String startDate = "2024-11-01";
        String endDate = "2024-11-30";

        LocalDateTime startAt = promotionInventory.convertStringToLocalDateTime(startDate);
        LocalDateTime endAt = promotionInventory.convertStringToLocalDateTime(endDate);

        assertTrue(startAt.isEqual(LocalDateTime.of(2024, 11, 1, 0, 0)));
        assertTrue(endAt.isEqual(LocalDateTime.of(2024, 11, 30, 0, 0)));
    }

}
