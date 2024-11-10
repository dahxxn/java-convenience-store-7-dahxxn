package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dto.TotalProductInfoDto;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingInfoTest {
    ShoppingInfo shoppingInfo;

    @BeforeEach
    public void setUp() {
        PromotionInventory promotionInventory = new PromotionInventory();
        ProductInventory productInventory = new ProductInventory(promotionInventory);
        shoppingInfo = new ShoppingInfo(productInventory, promotionInventory);
    }

    public void addProductForTest() {
        HashMap<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 3); //2+1
        purchaseInfo.put("사이다", 5);
        shoppingInfo.addProduct(purchaseInfo);
        shoppingInfo.addPromotionProductQuantity("콜라", 1);
        shoppingInfo.addPromotionProductQuantity("사이다", 1);
    }

    @Test
    public void 상품추가_기능_및_총_쇼핑_상품_정보_반환_테스트() {
        addProductForTest();
        List<TotalProductInfoDto> totalProductInfoDto = shoppingInfo.getTotalProductInfo();
        for (TotalProductInfoDto productInfoDto : totalProductInfoDto) {
            if (productInfoDto.getProductName().equals("콜라")) {
                assertEquals(productInfoDto.getProductQuantity(), 3);
                continue;
            }
            if (productInfoDto.getProductName().equals("사이다")) {
                assertEquals(productInfoDto.getProductQuantity(), 5);
            }
        }
    }

    @Test
    public void 상품_수량_추가_테스트() {
        addProductForTest();
        shoppingInfo.addProductQuantity("콜라", 2);

        int expectedQuantityForCola = 5;
        List<TotalProductInfoDto> totalProductInfoDto = shoppingInfo.getTotalProductInfo();
        for (TotalProductInfoDto productInfoDto : totalProductInfoDto) {
            if (productInfoDto.getProductName().equals("콜라")) {
                assertEquals(expectedQuantityForCola, productInfoDto.getProductQuantity());
            }
        }
    }

    @Test
    public void 상품_수량_차감_및_삭제_테스트() {
        addProductForTest();
        shoppingInfo.subProductQuantity("콜라", 3);

        assertEquals(0, shoppingInfo.getTotalProductInfo().stream()
                .filter(info -> info.getProductName().equals("콜라"))
                .count());
    }

    @Test
    public void 프로모션_제품_수량_증가_테스트() {
        addProductForTest();
        shoppingInfo.addPromotionProductQuantity("콜라", 1);

        int expectedPromotionQuantity = 2;
        assertEquals(expectedPromotionQuantity, shoppingInfo.getTotalPromotionInfo().stream()
                .filter(info -> info.getProductName().equals("콜라"))
                .findFirst()
                .get()
                .getProductQuantity());
    }

    @Test
    public void 총가격계산_기능_테스트() {
        addProductForTest();

        int expectedTotalPrice = 8000;
        assertEquals(expectedTotalPrice, shoppingInfo.calculateTotalPrice());
    }

    @Test
    public void 프로모션_할인_가격_계산_테스트() {
        addProductForTest();

        int expectedPrice = 2000;
        assertEquals(expectedPrice, shoppingInfo.calculateTotalPromotionPrice());
    }

    @Test
    public void 프로모션_할인_비적용_가격_계산_테스트() {
        addProductForTest();

        int expectedPrice = 2000;
        assertEquals(expectedPrice, shoppingInfo.calculateNonPromotionPrice());
    }

    @Test
    public void 멤버십_할인_가격_계산_테스트_멤버십_적용() {
        addProductForTest();
        shoppingInfo.setMemberShip(true);

        int expectedPrice = (int) (shoppingInfo.calculateNonPromotionPrice() * 0.3);
        assertEquals(expectedPrice, shoppingInfo.calculateMemberShipDiscountPrice());
    }

    @Test
    public void 멤버십_할인_가격_계산_테스트_멤버십_미적용() {
        addProductForTest();
        shoppingInfo.setMemberShip(false);

        int expectedPrice = 0;
        assertEquals(expectedPrice, shoppingInfo.calculateMemberShipDiscountPrice());
    }

    @Test
    public void 멤버십_최대_할인_테스트() {
        HashMap<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("물", 100);
        shoppingInfo.addProduct(purchaseInfo);
        addProductForTest();
        shoppingInfo.setMemberShip(true);

        int maxDiscount = 8000;
        assertEquals(maxDiscount, shoppingInfo.getMemberShipDiscountPrice());
    }

    @Test
    public void 내야할_돈_계산_테스트_멤버십적용() {
        addProductForTest();
        shoppingInfo.setMemberShip(true);

        int expectedPrice = 5400;
        assertEquals(expectedPrice, shoppingInfo.getRealCost());
    }

    @Test
    public void 내야할_돈_계산_테스트_멤버십미적용() {
        addProductForTest();
        shoppingInfo.setMemberShip(false);

        int expectedPrice = 6000;
        assertEquals(expectedPrice, shoppingInfo.getRealCost());
    }

    @Test
    public void 제품이_없을때_총_가격_및_수량_테스트() {
        int expectedQuantity = 0;
        int expectedTotalPrice = 0;

        assertEquals(expectedQuantity, shoppingInfo.calculateTotalCount());
        assertEquals(expectedTotalPrice, shoppingInfo.calculateTotalPrice());
        assertEquals(expectedTotalPrice, shoppingInfo.calculateTotalPromotionPrice());
        assertEquals(expectedTotalPrice, shoppingInfo.calculateMemberShipDiscountPrice());
        assertEquals(expectedTotalPrice, shoppingInfo.getRealCost());
    }
}
