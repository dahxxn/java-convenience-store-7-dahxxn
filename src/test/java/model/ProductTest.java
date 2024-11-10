package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {
    Product product;
    Product promotionProduct;

    @BeforeEach
    public void setUp() {
        promotionProduct = new Product("콜라", 10, 1000, "반짝할인");
        product = new Product("사이다", 0, 1500, Product.NULL_PROMOTION);
    }

    @Test
    @DisplayName("상품 가격 정보 반환 테스트")
    public void 상품_가격_정보_반환_테스트() {
        assertEquals(1000, promotionProduct.getPrice());
        assertEquals(1500, product.getPrice());
    }

    @Test
    @DisplayName("상품 수량 정보 반환 테스트")
    public void 상품_수량_정보_반환_테스트() {
        assertEquals(10, promotionProduct.getQuantity());
        assertEquals(0, product.getQuantity());
    }

    @Test
    @DisplayName("상품 수량 증가 테스트")
    public void 상품_수량_증가_테스트() {
        promotionProduct.addStock(5);
        assertEquals(15, promotionProduct.getQuantity());
    }

    @Test
    @DisplayName("상품 수량 감소 테스트")
    public void 상품_수량_감소_테스트() {
        promotionProduct.subStock(3);
        assertEquals(7, promotionProduct.getQuantity());
    }

    @Test
    @DisplayName("프로모션 정보 반환 테스트")
    public void 프로모션_정보_반환_테스트() {
        assertEquals("반짝할인", promotionProduct.getPromotion());
        assertEquals(Product.NULL_PROMOTION, product.getPromotion());
    }

    @Test
    @DisplayName("재고 메시지 생성 테스트")
    public void 재고_메시지_생성_테스트() {
        assertEquals(" 10개", promotionProduct.makeQuantityMessage());
        assertEquals(" 재고 없음", product.makeQuantityMessage());
    }

    @Test
    @DisplayName("프로모션 메시지 생성 테스트")
    public void 프로모션_메시지_생성_테스트() {
        assertEquals(" 반짝할인", promotionProduct.makePromotionMessage());
        assertEquals("", product.makePromotionMessage());
    }

    @Test
    @DisplayName("상품 정보 반환 테스트")
    public void 상품_정보_반환_테스트() {
        String expectedInfoWithPromotion = "- 콜라 1,000원 10개 반짝할인";
        String expectedInfoWithoutPromotion = "- 사이다 1,500원 재고 없음";

        assertEquals(expectedInfoWithPromotion, promotionProduct.getProductStockInfo());
        assertEquals(expectedInfoWithoutPromotion, product.getProductStockInfo());
    }

}
