package dto;

public class TotalProductInfoDto {
    String productName;
    int productQuantity;
    int totalProductPrice;

    public TotalProductInfoDto(String productName, int productQuantity, int totalProductPrice) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.totalProductPrice = totalProductPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }
}
