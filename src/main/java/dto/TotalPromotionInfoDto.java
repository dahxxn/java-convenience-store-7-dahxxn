package dto;

public class TotalPromotionInfoDto {
    String productName;
    int productQuantity;

    public TotalPromotionInfoDto(String productName, int productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}
