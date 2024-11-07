package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {
    Product product;

    @BeforeEach
    public void setUp() {
        product = new Product("콜라", 10, 1000, null);
    }

    @Test
    @DisplayName("상품 가격 정보 반환 테스트입니다.")
    public void 상품_가격_정보_반환_테스트() {
        assertEquals(1000, product.getPrice());
    }
}
