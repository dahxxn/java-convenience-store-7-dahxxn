package dto;

public class TotalCostInfoDto {
    int totalQuantity;
    int totalCost;

    public TotalCostInfoDto(int totalQuantity, int totalCost) {
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalCost() {
        return totalCost;
    }
}
