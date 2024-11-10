package controller;

import java.util.HashMap;
import model.ProductInventory;
import model.PromotionInventory;
import model.ShoppingInfo;
import service.DiscountService;
import view.InputView;
import view.OutputMessage;
import view.OutputView;

public class OrderController {
    private static PromotionInventory promotionInventory;
    private static ProductInventory productInventory;
    private static InputView inputView;
    private static OutputView outputView;
    private static ShoppingInfo shoppingInfo;

    public void settingInventory() {
        promotionInventory = new PromotionInventory();
        productInventory = new ProductInventory(promotionInventory);
        shoppingInfo = new ShoppingInfo(productInventory, promotionInventory);
    }

    public void settingView() {
        inputView = new InputView(productInventory);
        outputView = new OutputView();
    }

    public void showProductInventory() {
        outputView.printCurrentStock(productInventory.getProductStockInfo());
    }

    public HashMap<String, Integer> getPurchaseInfo() {
        String guideMessage = OutputMessage.INPUT_PURCHASE_INFO_GUIDE_MESSAGE
                + OutputMessage.INPUT_PURCHASE_INFO_EXAMPLE_MESSAGE.toString();
        return inputView.readPurchaseInfo(guideMessage);
    }

    public void startOrder() {
        outputView.printWelcomeMessage();
        showProductInventory();

        HashMap<String, Integer> purchaseInfo = getPurchaseInfo();

        shoppingInfo.addProduct(purchaseInfo);
    }

    public void checkOrder() {
        DiscountService discountService = new DiscountService(shoppingInfo, productInventory, promotionInventory);
        discountService.checkAllPromotionDiscount();
        discountService.checkMemberShipDiscount();
    }

    public void showReceipts() {
        outputView.printReceipt(shoppingInfo.getTotalProductInfo(), shoppingInfo.getTotalPromotionInfo(),
                shoppingInfo.getTotalCostInfo(), shoppingInfo.getPromotionDiscountPrice(),
                shoppingInfo.getMemberShipDiscountPrice(), shoppingInfo.getRealCost());
        shoppingInfo.updateProductInventory();
    }

    public boolean showContinue() {
        return inputView.readYesOrNoInfo(
                OutputMessage.THANK_YOU_AND_CONTINUE_MESSAGE + OutputMessage.INPUT_YES_OR_NO_GUIDE_MESSAGE.toString());
    }

    public void resetShoppingInfo() {
        shoppingInfo = new ShoppingInfo(productInventory, promotionInventory);
    }

    public void run() {
        settingInventory();
        settingView();

        do {
            resetShoppingInfo();
            startOrder();
            checkOrder();
            showReceipts();
        } while (showContinue());
    }
}
