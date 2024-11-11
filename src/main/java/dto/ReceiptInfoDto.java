package dto;

import java.util.List;

public record ReceiptInfoDto(List<TotalProductInfoDto> totalProductInfo,
                             List<TotalPromotionInfoDto> totalPromotionProductInfo, TotalCostInfoDto totalCostInfo,
                             int promotionDiscount, int membershipDiscount, int realCost) {
}
