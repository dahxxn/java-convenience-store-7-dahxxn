package view;

import dto.TotalCostInfoDto;
import dto.TotalProductInfoDto;
import dto.TotalPromotionInfoDto;
import java.util.List;

public class OutputView {

    public void printLineBreak() {
        System.out.println();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printWelcomeMessage() {
        printMessage(OutputMessage.WELCOME_MESSAGE.toString());
        printMessage(OutputMessage.CURRENT_INVENTORY_GUIDE_MESSAGE.toString());
        printLineBreak();
    }

    public void printCurrentStock(List<String> currentInventoryStock) {
        for (String stock : currentInventoryStock) {
            printMessage(stock);
        }
        printLineBreak();
    }

    public void printTotalProductInfo(List<TotalProductInfoDto> totalProductInfo) {
        printMessage(OutputMessage.RECEIPTS_START_LINE.toString());
        printMessage(OutputMessage.RECEIPTS_TOTAL_PRODUCT_SUBJECT.toString());

        for (TotalProductInfoDto productInfo : totalProductInfo) {
            String formattedProductInfo = OutputMessage.RECEIPTS_PRODUCT_INFO.formatMessage(
                    productInfo.getProductName(),
                    productInfo.getProductQuantity(),
                    productInfo.getTotalProductPrice()
            );
            printMessage(formattedProductInfo);
        }
    }

    public void printTotalPromotionProductInfo(List<TotalPromotionInfoDto> totalPromotionProductInfo) {
        printMessage(OutputMessage.RECEIPTS_PROMOTION_LINE.toString());

        for (TotalPromotionInfoDto promotionInfo : totalPromotionProductInfo) {
            printMessage(String.format(OutputMessage.RECEIPTS_PROMOTION_PRODUCT_INFO.toString(),
                    promotionInfo.getProductName(), promotionInfo.getProductQuantity()));
        }
    }

    public void printTotalCostInfo(TotalCostInfoDto totalCostInfo, int promotionDiscount, int membershipDiscount,
                                   int realCost) {
        printMessage(OutputMessage.RECEIPTS_DIVIDER.toString());
        printMessage(
                String.format(OutputMessage.RECEIPTS_PRODUCT_INFO.toString(), "총구매액", totalCostInfo.getTotalQuantity(),
                        totalCostInfo.getTotalCost()));
        printMessage(String.format(OutputMessage.RECEIPTS_DISCOUNT_COST_INFO.toString(), "행사할인", promotionDiscount));
        printMessage(String.format(OutputMessage.RECEIPTS_DISCOUNT_COST_INFO.toString(), "멤버십할인", membershipDiscount));
        printMessage(String.format(OutputMessage.RECEIPTS_COST_INFO.toString(), "내실돈", realCost));
    }

    public void printReceipt(List<TotalProductInfoDto> totalProductInfo,
                             List<TotalPromotionInfoDto> totalPromotionProductInfo, TotalCostInfoDto totalCostInfo,
                             int promotionDiscount, int membershipDiscount, int realCost) {
        printTotalProductInfo(totalProductInfo);
        printTotalPromotionProductInfo(totalPromotionProductInfo);
        printTotalCostInfo(totalCostInfo, promotionDiscount, membershipDiscount, realCost);
    }


}
