package model;

import constant.PriceMetadata;
import dto.TotalCostInfoDto;
import dto.TotalProductInfoDto;
import dto.TotalPromotionInfoDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ShoppingInfo {
    private static final int MAX_MEMBERSHIP_DISCOUNT = PriceMetadata.MAX_MEMBERSHIP_DISCOUNT_PRICE.getValue();
    private static final double MEMBERSHIP_DISCOUNT_PERCENTS =
            (double) PriceMetadata.MEMBERSHIP_DISCOUNT_PERCENTS.getValue() / 100;
    private final LinkedHashMap<String, Integer> product;
    private final LinkedHashMap<String, Integer> promotionProduct;
    private final HashMap<String, Integer> productPrice;
    private boolean isMemberShip;
    private final ProductInventory productInventory;
    private final PromotionInventory promotionInventory;

    public ShoppingInfo(ProductInventory productInventory, PromotionInventory promotionInventory) {
        this.product = new LinkedHashMap<>();
        this.promotionProduct = new LinkedHashMap<>();
        this.productPrice = new HashMap<>();
        this.isMemberShip = false;
        this.productInventory = productInventory;
        this.promotionInventory = promotionInventory;
    }

    public void addProduct(LinkedHashMap<String, Integer> purchaseInfo) {
        for (String productName : purchaseInfo.keySet()) {
            int productQuantity = purchaseInfo.get(productName);
            Product product = productInventory.findProduct(productName);
            int productPrice = product.getPrice();
            this.product.put(productName, productQuantity);
            this.productPrice.put(productName, productPrice);
        }
    }

    public void addProductQuantity(String productName, int productQuantity) {
        if (productQuantity > 0) {
            int newQuantity = productQuantity;
            if (this.product.containsKey(productName)) {
                newQuantity += this.product.get(productName);
            }
            this.product.put(productName, newQuantity);
        }
    }

    public void subProductQuantity(String productName, int productQuantity) {
        int updateQuantity = product.get(productName) - productQuantity;
        product.put(productName, updateQuantity);
    }

    public void addPromotionProductQuantity(String productName, int productQuantity) {
        if (productQuantity > 0) {
            int newQuantity = productQuantity;
            if (this.promotionProduct.containsKey(productName)) {
                newQuantity += this.promotionProduct.get(productName);
            }
            this.promotionProduct.put(productName, newQuantity);
        }
    }

    public void setMemberShip(boolean isMemberShip) {
        this.isMemberShip = isMemberShip;
    }

    public List<TotalProductInfoDto> getTotalProductInfo() {
        List<TotalProductInfoDto> totalProductInfoDtos = new ArrayList<>();
        for (String productName : this.product.keySet()) {
            totalProductInfoDtos.add(createTotalProductInfoDto(productName));
        }
        return totalProductInfoDtos;
    }

    private TotalProductInfoDto createTotalProductInfoDto(String productName) {
        int productQuantity = this.product.get(productName);
        int productPrice = this.productPrice.get(productName);
        int totalPrice = productPrice * productQuantity;
        return new TotalProductInfoDto(productName, productQuantity, totalPrice);
    }

    public List<TotalPromotionInfoDto> getTotalPromotionInfo() {
        List<TotalPromotionInfoDto> totalPromotionInfoDtos = new ArrayList<>();
        for (String productName : this.promotionProduct.keySet()) {
            int productQuantity = this.promotionProduct.get(productName);
            TotalPromotionInfoDto totalPromotionInfoDto = new TotalPromotionInfoDto(productName, productQuantity);
            totalPromotionInfoDtos.add(totalPromotionInfoDto);
        }
        return totalPromotionInfoDtos;
    }

    public TotalCostInfoDto getTotalCostInfo() {
        int totalQuantity = calculateTotalCount();
        int totalPrice = calculateTotalPrice();
        return new TotalCostInfoDto(totalQuantity, totalPrice);
    }

    public int getPromotionDiscountPrice() {
        return calculateTotalPromotionPrice();
    }

    public int getMemberShipDiscountPrice() {
        if (!this.isMemberShip) {
            return 0;
        }
        return calculateMemberShipDiscountPrice();
    }

    public int getRealCost() {
        int totalCost = calculateTotalPrice();
        int totalPromotionDiscount = calculateTotalPromotionPrice();
        int membershipDiscount = calculateMemberShipDiscountPrice();
        return totalCost - totalPromotionDiscount - membershipDiscount;
    }

    public int calculateMemberShipDiscountPrice() {
        if (isMemberShip) {
            int notPromotionCost = calculateNonPromotionPrice();
            int membershipDiscount = (int) (notPromotionCost * MEMBERSHIP_DISCOUNT_PERCENTS);
            if (membershipDiscount > MAX_MEMBERSHIP_DISCOUNT) {
                return MAX_MEMBERSHIP_DISCOUNT;
            }
            return membershipDiscount;
        }
        return 0;
    }

    public int calculateTotalCount() {
        int totalCount = 0;
        for (String productName : product.keySet()) {
            totalCount += product.get(productName);
        }
        return totalCount;
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (String productName : this.productPrice.keySet()) {
            totalPrice += this.productPrice.get(productName) * this.product.get(productName);
        }
        return totalPrice;
    }

    public int calculateTotalPromotionPrice() {
        int totalPromotionPrice = 0;
        for (String productName : this.promotionProduct.keySet()) {
            totalPromotionPrice += this.productPrice.get(productName) * promotionProduct.get(productName);
        }
        return totalPromotionPrice;
    }

    public int calculateNonPromotionPrice() {
        int totalPrice = calculateTotalPrice();
        int totalPromotionPrice = calculateAllPromotionPrice();
        return totalPrice - totalPromotionPrice;
    }

    public int calculateAllPromotionPrice() {
        int totalPromotionPrice = 0;
        for (String productName : this.promotionProduct.keySet()) {
            totalPromotionPrice += calculateProductPromotionPrice(productName);
        }
        return totalPromotionPrice;
    }

    private int calculateProductPromotionPrice(String productName) {
        Product product = productInventory.findPromotionProduct(productName);
        int promotionQuantity = this.promotionProduct.get(productName);
        int productPrice = this.productPrice.get(productName);

        Promotion promotion = promotionInventory.findPromotion(product.getPromotion());
        int totalCount = promotion.getTotalCount();
        return totalCount * promotionQuantity * productPrice;
    }

    public void updateProductInventory() {
        List<TotalProductInfoDto> totalProductInfoDtos = this.getTotalProductInfo();
        for (TotalProductInfoDto totalProductInfoDto : totalProductInfoDtos) {
            String productName = totalProductInfoDto.getProductName();
            int productQuantity = totalProductInfoDto.getProductQuantity();
            productInventory.outTotalProduct(productName, productQuantity);
        }
    }
}
