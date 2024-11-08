package model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import error.ExceptionMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductInventoryTest {

    private final ProductInventory productInventory = new ProductInventory();

    @Test
    @DisplayName("상품 인벤토리에 새로운 프로모션 상품 정보를 추가하는 테스트입니다. ")
    public void 프로모션_상품_추가_테스트() {
        String productName = "환타";
        int quantity = 100;
        int price = 1500;
        String promotion = "탄산2+1";

        productInventory.addProduct(productName, quantity, price, promotion);

        assertFalse(productInventory.hasProduct(productName));
        assertTrue(productInventory.hasPromotionProduct(productName));
    }

    @Test
    @DisplayName("상품 인벤토리에 새로운 프로모션이 없는 상품 정보를 추가하는 테스트입니다. ")
    public void 프로모션_없는_상품_추가_테스트() {
        String productName = "제로제로콜라";
        int quantity = 200;
        int price = 1000;
        String promotion = "null";

        productInventory.addProduct(productName, quantity, price, promotion);

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

        assertThatThrownBy(() -> productInventory.addProduct(productName, quantity, price, promotion))
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
        int requestedQuantity = 5;

        productInventory.addProduct(productName, 10, 1500, "null");

        assertDoesNotThrow(() -> productInventory.isCanBuyProduct(productName, requestedQuantity));
    }

    @Test
    @DisplayName("상품과 구매 수량을 통해, 구매 수량이 판매재고보다 많으면 예외를 발생시키는 테스트입니다. ")
    public void 구매_가능_여부_예외_테스트() {
        String productName = "환타";
        int requestedQuantity = 200;

        productInventory.addProduct(productName, 10, 1500, "null");

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

        productInventory.addProduct(productName, stockQuantity, 1500, "null");
        productInventory.addProduct(productName, promotionProductQuantity, 1500, "탄산2+1");

        int totalCount = productInventory.getTotalProductCount(productName);
        assertEquals(stockQuantity + promotionProductQuantity, totalCount);
    }

    @Test
    @DisplayName("상품 이름으로 해당 상품의 일반재고를 반환하는 테스트 입니다. ")
    public void 상품의_일반재고_반환_테스트() {
        String productName = "제로제로콜라";
        int quantity = 100;

        productInventory.addProduct(productName, quantity, 1000, "null");

        int productQuantity = productInventory.getProductQuantity(productName);
        assertEquals(quantity, productQuantity);
    }

    @Test
    @DisplayName("상품 이름으로 프로모션 재고를 반환하는 테스트 입니다. ")
    public void 상품의_프로모션_재고_반환_테스트() {
        String productName = "제로제로콜라";
        int quantity = 50;

        productInventory.addProduct(productName, quantity, 2000, "탄산2+1");

        int promotionProductQuantity = productInventory.getPromotionProductQuantity(productName);
        assertEquals(quantity, promotionProductQuantity);
    }

}