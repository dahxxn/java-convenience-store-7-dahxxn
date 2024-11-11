package model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dto.ProductInfoDto;
import error.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductInventoryTest {
    private static ProductInventory productInventory;

    @BeforeEach
    void setUp() {
        PromotionInventory promotionInventory = new PromotionInventory();
        productInventory = new ProductInventory(promotionInventory);
    }

    @Test
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
    public void 상품_이름으로_찾아서_반환_테스트() {
        String productName = "찾을 상품";
        int quantity = 100;
        int price = 1500;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, price, "null");
        productInventory.addProduct(productInfoDto);

        Product foundProduct = productInventory.findProduct(productName);
        assertEquals(productName, foundProduct.getName());
        assertEquals(quantity, foundProduct.getQuantity());
        assertEquals(price, foundProduct.getPrice());
    }

    @Test
    public void 프로모션_상품_이름으로_찾아서_반환_테스트() {
        String productName = "찾을 프로모션 상품";
        int quantity = 50;
        int price = 2000;
        String promotion = "탄산2+1";

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, price, promotion);
        productInventory.addProduct(productInfoDto);

        Product foundProduct = productInventory.findPromotionProduct(productName);
        assertEquals(productName, foundProduct.getName());
        assertEquals(quantity, foundProduct.getQuantity());
        assertEquals(price, foundProduct.getPrice());
        assertEquals(promotion, foundProduct.getPromotion());
    }


    @Test
    public void 상품_존재_여부_확인_테스트_존재하지_않을때() {
        String nonExistProductName = "존재하지않는상품";

        assertThatThrownBy(() -> productInventory.isProductExist(nonExistProductName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_NOT_EXIST_PRODUCT.toString());
    }

    @Test
    public void 상품_존재_여부_확인_테스트_존재할때() {
        String existProductName = "콜라";

        assertDoesNotThrow(() -> productInventory.isProductExist(existProductName));
    }

    @Test
    public void 구매_가능_여부_테스트() {
        String productName = "환타";

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, 10, 1500, "null");
        productInventory.addProduct(productInfoDto);

        int requestedQuantity = 5;

        assertDoesNotThrow(() -> productInventory.isCanBuyProduct(productName, requestedQuantity));
    }

    @Test
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
    public void 상품의_일반재고_반환_테스트() {
        String productName = "제로제로콜라";
        int quantity = 100;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, 1000, "null");
        productInventory.addProduct(productInfoDto);

        int productQuantity = productInventory.getProductQuantity(productName);
        assertEquals(quantity, productQuantity);
    }

    @Test
    public void 상품의_프로모션_재고_반환_테스트() {
        String productName = "제로제로콜라";
        int quantity = 50;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, quantity, 2000, "탄산2+1");
        productInventory.addProduct(productInfoDto);

        int promotionProductQuantity = productInventory.getPromotionProductQuantity(productName);
        assertEquals(quantity, promotionProductQuantity);
    }

    @Test
    public void 상품_총_재고_수량_반환_테스트() {
        String productName = "상품D";
        int generalStockQuantity = 50;
        int promotionStockQuantity = 30;

        ProductInfoDto productInfoDto = new ProductInfoDto(productName, generalStockQuantity, 1000, "null");
        ProductInfoDto promotionProductInfoDto = new ProductInfoDto(productName, promotionStockQuantity, 1500, "탄산2+1");

        productInventory.addProduct(productInfoDto);
        productInventory.addProduct(promotionProductInfoDto);

        int totalCount = productInventory.getTotalProductCount(productName);
        assertEquals(generalStockQuantity + promotionStockQuantity, totalCount);
    }

    @Test
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