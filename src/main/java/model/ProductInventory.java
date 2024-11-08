package model;

import Util.FileReaderUtil;
import error.CustomException;
import error.ExceptionMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInventory {
    private final List<Product> products = new ArrayList<>();
    private final List<Product> promotionProducts = new ArrayList<>();
    private final HashMap<String, Integer> promotionProductIndex = new HashMap<>();
    private final HashMap<String, Integer> productIndex = new HashMap<>();
    static final String PRODUCT_MD_FILE_PATH = "src/main/resources/products.md";
    static final String NULL_PROMOTION = "null";
    PromotionInventory promotionInventory;

    public ProductInventory() {
        this.promotionInventory = new PromotionInventory();
        setProductInventory();
    }

    public void setProductInfo(List<String> productInfo) {
        String name = productInfo.get(ProductInfoLineStructure.PRODUCT_NAME.ordinal());
        int price = Integer.parseInt(productInfo.get(ProductInfoLineStructure.PRODUCT_PRICE.ordinal()));
        int quantity = Integer.parseInt(
                productInfo.get(ProductInfoLineStructure.PRODUCT_QUANTITIES.ordinal()));
        String promotion = productInfo.get(ProductInfoLineStructure.PRODUCT_PROMOTION.ordinal());
        addProduct(name, quantity, price, promotion);
    }

    public void setProductInventory() {
        List<String> productStockInfos = FileReaderUtil.readFile(PRODUCT_MD_FILE_PATH);
        for (int i = 1; i < productStockInfos.size(); i++) {
            String productStockInfo = productStockInfos.get(i);
            List<String> productInfo = FileReaderUtil.delimiterLine(productStockInfo);
            setProductInfo(productInfo);
        }
    }

    public void addProduct(String name, int quantity, int price, String promotion) {
        Product product = new Product(name, quantity, price, promotion);
        if (promotion.equals(NULL_PROMOTION)) {
            productIndex.put(name, products.size());
            products.add(product);
            return;
        }
        if (!promotionInventory.hasPromotion(promotion)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_CANNOT_FIND_PROMOTION);
        }
        promotionProductIndex.put(name, promotionProductIndex.size());
        promotionProducts.add(product);
    }

    public boolean hasProduct(String productName) {
        return productIndex.containsKey(productName);
    }

    public boolean hasPromotionProduct(String productName) {
        return promotionProductIndex.containsKey(productName);
    }

    public void isProductExist(String productName) {
        if (!hasProduct(productName) && !hasPromotionProduct(productName)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_NOT_EXIST_PRODUCT);
        }
    }

    public void isCanBuyProduct(String productName, int productQuantity) {
        int stockQuantity = getTotalProductCount(productName);
        if (stockQuantity < productQuantity) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_OUT_OF_STOCK);
        }
    }

    public int getProductQuantity(String productName) {
        if (hasProduct(productName)) {
            Product product = findProduct(productName);
            return product.getQuantity();
        }
        return 0;
    }

    public int getPromotionProductQuantity(String productName) {
        if (hasPromotionProduct(productName)) {
            Product promotionProduct = findPromotionProduct(productName);
            return promotionProduct.getQuantity();
        }
        return 0;
    }

    public int getTotalProductCount(String productName) {
        int productCount = getProductQuantity(productName);
        int promotionProductCount = getPromotionProductQuantity(productName);
        return productCount + promotionProductCount;
    }

    public Product findProduct(String productName) {
        int index = productIndex.get(productName);
        return products.get(index);
    }

    public Product findPromotionProduct(String productName) {
        int index = promotionProductIndex.get(productName);
        return promotionProducts.get(index);
    }
}
