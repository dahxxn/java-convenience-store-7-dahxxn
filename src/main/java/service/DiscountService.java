package service;

import dto.TotalProductInfoDto;
import java.util.List;
import model.Product;
import model.ProductInventory;
import model.Promotion;
import model.PromotionInventory;
import model.ShoppingInfo;
import view.InputView;
import view.OutputMessage;

public class DiscountService {
    private final ShoppingInfo shoppingInfo;
    private final ProductInventory productInventory;
    private final PromotionInventory promotionInventory;
    InputView inputView;

    public DiscountService(ShoppingInfo shoppingInfo, ProductInventory productInventory,
                           PromotionInventory promotionInventory) {
        this.shoppingInfo = shoppingInfo;
        this.productInventory = productInventory;
        this.promotionInventory = promotionInventory;
        this.inputView = new InputView(productInventory);
    }

    public Promotion getPromotionWithName(String productName) {
        Product promotionProduct = productInventory.findPromotionProduct(productName);
        String promotionName = promotionProduct.getPromotion();
        return promotionInventory.findPromotion(promotionName);
    }

    public boolean canBuyPromotionProduct(String productName) {
        if (!productInventory.hasPromotionProduct(productName)) {
            return false;
        }
        Promotion promotion = getPromotionWithName(productName);
        return promotion.isPromotionNow();
    }

    public void handlePromotionImpossible(String productName, Promotion promotion, int buyCount, int setCount,
                                          int promotionStock) {
        setCount *= (-1);
        int promotionSetCount = promotion.checkPromotionPossible(buyCount - setCount, promotionStock);
        shoppingInfo.addPromotionProductQuantity(productName, promotionSetCount);

        String guideMessage = OutputMessage.INPUT_NON_PROMOTION_GUIDE_MESSAGE.formatMessage(productName, setCount)
                + OutputMessage.INPUT_YES_OR_NO_GUIDE_MESSAGE;
        boolean answer = inputView.readYesOrNoInfo(guideMessage);
        if (!answer) {
            shoppingInfo.subProductQuantity(productName, setCount);
        }
    }

    public void handlePromotionPossible(String productName, Promotion promotion, int remainBuyCount,
                                        int remainStockCount) {
        if (promotion.checkGetMorePromotion(remainBuyCount, remainStockCount)) {
            String guideMessage =
                    OutputMessage.INPUT_PROMOTION_GUIDE_MESSAGE.formatMessage(productName, promotion.getGetCount())
                            + OutputMessage.INPUT_YES_OR_NO_GUIDE_MESSAGE;
            boolean answer = inputView.readYesOrNoInfo(guideMessage);
            if (answer) {
                shoppingInfo.addPromotionProductQuantity(productName, promotion.getGetCount());
                shoppingInfo.addProductQuantity(productName, promotion.getGetCount());
            }
        }
    }

    public void calculatePromotionProduct(String productName, int buyCount) {
        Promotion promotion = getPromotionWithName(productName);
        int promotionStock = productInventory.getPromotionProductQuantity(productName);
        int setCount = promotion.checkPromotionPossible(buyCount, promotionStock);
        if (setCount < 0) {
            handlePromotionImpossible(productName, promotion, buyCount, setCount, promotionStock);
            return;
        }
        shoppingInfo.addPromotionProductQuantity(productName, setCount);
        int remainBuyCount = promotion.calculateRemainingCount(setCount, buyCount);
        int remainStockCount = promotion.calculateRemainingCount(setCount, promotionStock);
        handlePromotionPossible(productName, promotion, remainBuyCount, remainStockCount);
    }

    public void checkAllPromotionDiscount() {
        List<TotalProductInfoDto> totalProductInfos = shoppingInfo.getTotalProductInfo();
        for (TotalProductInfoDto productInfo : totalProductInfos) {
            String productName = productInfo.getProductName();
            int buyCount = productInfo.getProductQuantity();
            if (canBuyPromotionProduct(productName)) {
                calculatePromotionProduct(productName, buyCount);
            }
        }
    }

    public void checkMemberShipDiscount() {
        String guideMessage =
                OutputMessage.INPUT_MEMBERSHIP_GUID_MESSAGE + OutputMessage.INPUT_YES_OR_NO_GUIDE_MESSAGE.toString();

        boolean answer = inputView.readYesOrNoInfo(guideMessage);
        shoppingInfo.setMemberShip(answer);
    }

}