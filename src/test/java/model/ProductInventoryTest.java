package model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dto.ProductInfoDto;
import error.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductInventoryTest {
    private static ProductInventory productInventory;

    @BeforeEach
    void setUp() {
        PromotionInventory promotionInventory = new PromotionInventory();
        productInventory = new ProductInventory(promotionInventory);
    }

    @Test
    @DisplayName("상품 인벤토리에 새로운 프로모션 상품 정보를 추가하는 테스트입니다. ")
    public void 프로모션_상품_추가_테스트() {
        String productName = "환타";
        int quantity = 100;
        int price = 1500;
        String promotion = "탄산2+1";

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, price, promotion);
        productInventory.addProduct(productInfoDto);

        assertTrue(productInventory.hasProduct(productName));
        assertTrue(productInventory.hasPromotionProduct(productName));
    }

    @Test
    @DisplayName("상품 인벤토리에 새로운 프로모션이 없는 상품 정보를 추가하는 테스트입니다. ")
    public void 프로모션_없는_상품_추가_테스트() {
        String productName = "제로제로콜라";
        int quantity = 200;
        int price = 1000;
        String promotion = "null";

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, price, promotion);
        productInventory.addProduct(productInfoDto);

        assertTrue(productInventory.hasProduct(productName));
        assertFalse(productInventory.hasPromotionProduct(productName));
    }

    @Test
    @DisplayName("상품 인벤토리에 존재하지 않는 프로모션을 가진 상품 정보를 추가할때 예외가 발생하는 테스트입니다. ")
    public void 프로모션_상품_추가_예외_테스트() {
        String productName = "상품A";
        int quantity = 50;
        int price = 2500;
        String promotion = "존재하지 않는 프로모션";
        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, price, promotion);

        assertThatThrownBy(() -> productInventory.addProduct(productInfoDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_CANNOT_FIND_PROMOTION.toString());
    }

    @Test
    @DisplayName("상품 인벤토리에 존재하지 않는 상품을 확인하는 경우 예외를 발생시키는 테스트입니다. ")
    public void 상품_존재_여부_확인_테스트_존재하지_않을때() {
        String nonExistProductName = "존재하지않는상품";

        assertThatThrownBy(() -> productInventory.isProductExist(nonExistProductName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_NOT_EXIST_PRODUCT.toString());
    }

    @Test
    @DisplayName("상품 인벤토리에 존재하는 상품을 확인하는 경우 아무런 예외가 발생하지 않는 테스트입니다. ")
    public void 상품_존재_여부_확인_테스트_존재할때() {
        String existProductName = "콜라";

        assertDoesNotThrow(() -> productInventory.isProductExist(existProductName));
    }

    @Test
    @DisplayName("상품과 구매 수량을 통해 구매 가능 여부를 확인하는 테스트입니다. ")
    public void 구매_가능_여부_테스트() {
        String productName = "환타";

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, 10, 1500, "null");
        productInventory.addProduct(productInfoDto);

        int requestedQuantity = 5;

        assertDoesNotThrow(() -> productInventory.isCanBuyProduct(productName, requestedQuantity));
    }

    @Test
    @DisplayName("상품과 구매 수량을 통해, 구매 수량이 판매재고보다 많으면 예외를 발생시키는 테스트입니다. ")
    public void 구매_가능_여부_예외_테스트() {
        String productName = "환타";

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, 10, 1500, "null");
        productInventory.addProduct(productInfoDto);

        int requestedQuantity = 200;
        assertThatThrownBy(() -> productInventory.isCanBuyProduct(productName, requestedQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_OUT_OF_STOCK.toString());
    }


    @Test
    @DisplayName("상품 이름으로 해당 상품의 총 재고(일반재고+프로모션재고)를 반환하는 테스트입니다. ")
    public void 상품의_총_재고_반환_테스트() {
        String productName = "환타";
        int stockQuantity = 100;
        int promotionProductQuantity = 50;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, stockQuantity, 1500, "null");
        ProductInfoDto promotionProductInfoDto = new ProductInfoDto(productName, promotionProductQuantity, 1500,
                "탄산2+1");

        productInventory.addProduct(productInfoDto);
        productInventory.addProduct(promotionProductInfoDto);

        int totalCount = productInventory.getTotalProductCount(productName);
        assertEquals(stockQuantity + promotionProductQuantity, totalCount);
    }

    @Test
    @DisplayName("상품 이름으로 해당 상품의 일반재고를 반환하는 테스트 입니다. ")
    public void 상품의_일반재고_반환_테스트() {
        String productName = "제로제로콜라";
        int quantity = 100;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, 1000, "null");
        productInventory.addProduct(productInfoDto);

        int productQuantity = productInventory.getProductQuantity(productName);
        assertEquals(quantity, productQuantity);
    }

    @Test
    @DisplayName("상품 이름으로 프로모션 재고를 반환하는 테스트 입니다. ")
    public void 상품의_프로모션_재고_반환_테스트() {
        String productName = "제로제로콜라";
        int quantity = 50;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, 2000, "탄산2+1");
        productInventory.addProduct(productInfoDto);

        int promotionProductQuantity = productInventory.getPromotionProductQuantity(productName);
        assertEquals(quantity, promotionProductQuantity);
    }

    @Test
    @DisplayName("일반 상품의 재고를 올바르게 차감하는 테스트입니다.")
    public void 상품_재고_차감_테스트() {
        String productName = "제로제로콜라";
        int initialQuantity = 50;
        int subtractQuantity = 20;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, initialQuantity, 1000, "null");
        productInventory.addProduct(productInfoDto);

        int remainingQuantity = productInventory.outProduct(productName, subtractQuantity);

        assertEquals(remainingQuantity, 0);
        assertEquals(productInventory.getProductQuantity(productName), initialQuantity - subtractQuantity);
    }

    @Test
    @DisplayName("프로모션 상품의 재고를 올바르게 차감하는 테스트입니다.")
    public void 프로모션_상품_재고_차감_테스트() {
        String productName = "환타";
        int initialQuantity = 30;
        int subtractQuantity = 10;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, initialQuantity, 1200, "탄산2+1");
        productInventory.addProduct(productInfoDto);
        int remainingQuantity = productInventory.outPromotionProduct(productName, subtractQuantity);

        assertEquals(remainingQuantity, 0);
        assertEquals(productInventory.getPromotionProductQuantity(productName), initialQuantity - subtractQuantity);
    }

    @Test
    @DisplayName("상품의 일반 재고와 프로모션 재고를 함께 차감하는 테스트입니다.")
    public void 총_재고_차감_테스트() {
        String productName = "제로제로콜라";
        int generalStockQuantity = 40;
        int promotionStockQuantity = 20;
        int totalSubtractQuantity = 45;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, generalStockQuantity, 1100, "null");
        ProductInfoDto promotionProductInfoDto = new ProductInfoDto(productName, promotionStockQuantity, 1100,
                "탄산2+1");

        productInventory.addProduct(productInfoDto);
        productInventory.addProduct(promotionProductInfoDto);
        productInventory.outTotalProduct(productName, totalSubtractQuantity);
        assertEquals(productInventory.getProductQuantity(productName), 15);
        assertEquals(productInventory.getPromotionProductQuantity(productName), 0);
    }
}