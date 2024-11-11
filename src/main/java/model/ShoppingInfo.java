package model;

import dto.TotalCostInfoDto;
import dto.TotalProductInfoDto;
import dto.TotalPromotionInfoDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingInfo {
    private final HashMap<String, Integer> product;
    private final HashMap<String, Integer> promotionProduct;
    private final HashMap<String, Integer> productPrice;
    private boolean isMemberShip;
    private final ProductInventory productInventory;
    private final PromotionInventory promotionInventory;
    private static double MEMBERSHIP_DISCOUNT_PERCENTIES = 0.3;
    private static int MAX_MEMBERSHIP_DISCOUNT = 8000;

    public ShoppingInfo(ProductInventory productInventory, PromotionInventory promotionInventory) {
        this.product = new HashMap<>();
        this.promotionProduct = new HashMap<>();
        this.productPrice = new HashMap<>();
        this.isMemberShip = false;
        this.productInventory = productInventory;
        this.promotionInventory = promotionInventory;
    }

    public void addProduct(HashMap<String, Integer> purchaseInfo) {
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
        if (updateQuantity == 0) {
            product.remove(productName);
        }
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
            int productQuantity = this.product.get(productName);
            int productPrice = this.productPrice.get(productName);
            int totalPrice = productPrice * productQuantity;
            TotalProductInfoDto totalProductInfoDto = new TotalProductInfoDto(productName, productQuantity, totalPrice);
            totalProductInfoDtos.add(totalProductInfoDto);
        }
        return totalProductInfoDtos;
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
            int membershipDiscount = (int) (notPromotionCost * MEMBERSHIP_DISCOUNT_PERCENTIES);
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
        int totalPromotionPrice = 0;
        int totalPrice = calculateTotalPrice();
        for (String productName : this.promotionProduct.keySet()) {
            Product product = productInventory.findPromotionProduct(productName);
            String promotionName = product.getPromotion();
            Promotion promotion = promotionInventory.findPromotion(promotionName);
            int totalCount = promotion.getTotalCount();
            totalPromotionPrice += (totalCount * this.promotionProduct.get(productName) * productPrice.get(
                    productName));
        }
        return totalPrice - totalPromotionPrice;
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