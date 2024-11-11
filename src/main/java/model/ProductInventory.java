package model;

import Util.FileReaderUtil;
import constant.FileMetadata;
import dto.ProductInfoDto;
import error.CustomException;
import error.ExceptionMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInventory {
    private static final String PRODUCT_MD_FILE_PATH = FileMetadata.PRODUCT_MD_FILE_PATH.toString();
    private static final String NULL_PROMOTION = FileMetadata.NULL_PROMOTION.toString();
    private final List<Product> products = new ArrayList<>();
    private final List<Product> promotionProducts = new ArrayList<>();
    private final HashMap<String, Integer> promotionProductIndex = new HashMap<>();
    private final HashMap<String, Integer> productIndex = new HashMap<>();
    private final PromotionInventory promotionInventory;

    public ProductInventory(PromotionInventory promotionInventory) {
        this.promotionInventory = promotionInventory;
        setProductInventory();
    }

    public List<String> getProductStockInfo() {
        List<String> totalProductStockInfo = new ArrayList<>();
        for (Product product : products) {
            if (hasPromotionProduct(product.getName())) {
                Product promotionProduct = findPromotionProduct(product.getName());
                totalProductStockInfo.add(promotionProduct.makeProductStockInfoMessage());
            }
            totalProductStockInfo.add(product.makeProductStockInfoMessage());
        }
        return totalProductStockInfo;
    }

    public void setProductInfo(List<String> productInfo) {
        String name = productInfo.get(ProductFileStructure.PRODUCT_NAME.ordinal());
        int price = Integer.parseInt(productInfo.get(ProductFileStructure.PRODUCT_PRICE.ordinal()));
        int quantity = Integer.parseInt(
                productInfo.get(ProductFileStructure.PRODUCT_QUANTITIES.ordinal()));
        String promotion = productInfo.get(ProductFileStructure.PRODUCT_PROMOTION.ordinal());
        ProductInfoDto productInfoDto = new ProductInfoDto(name, quantity, price, promotion);
        addProduct(productInfoDto);
    }

    public void setProductInventory() {
        List<String> productStockInfos = FileReaderUtil.readFile(PRODUCT_MD_FILE_PATH);
        for (int i = 1; i < productStockInfos.size(); i++) {
            String productStockInfo = productStockInfos.get(i);
            List<String> productInfo = FileReaderUtil.delimiterLine(productStockInfo);
            setProductInfo(productInfo);
        }
    }

    public void handleGeneralProductAdd(ProductInfoDto productInfoDto) {
        Product product = productInfoDto.generateProduct();
        String productName = productInfoDto.getName();
        if (productIndex.containsKey(productName)) {
            Product findProduct = findProduct(productName);
            findProduct.addStock(productInfoDto.getQuantity());
            return;
        }
        addNewProduct(productName, product);
    }

    public void addNewProduct(String name, Product product) {
        products.add(product);
        productIndex.put(name, products.size() - 1);
    }

    public void handlePromotionProductAdd(ProductInfoDto productInfoDto) {
        String productName = productInfoDto.getName();
        checkIfPromotionAlreadyExists(productName);
        addGeneralProductIfNotExists(productInfoDto);
        addPromotionProduct(productInfoDto);
    }

    private void checkIfPromotionAlreadyExists(String productName) {
        if (hasPromotionProduct(productName)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_PRODUCT_ALREADY_HAVE_PROMOTION);
        }
    }

    private void addGeneralProductIfNotExists(ProductInfoDto productInfoDto) {
        String productName = productInfoDto.getName();
        if (!productIndex.containsKey(productName)) {
            Product generalProduct = new Product(productName, 0, productInfoDto.getPrice(), NULL_PROMOTION);
            addNewProduct(productName, generalProduct);
        }
    }

    private void addPromotionProduct(ProductInfoDto productInfoDto) {
        Product promotionProduct = productInfoDto.generateProduct();
        promotionProducts.add(promotionProduct);
        promotionProductIndex.put(productInfoDto.getName(), promotionProducts.size() - 1);
    }

    public void addProduct(ProductInfoDto productInfoDto) {
        String promotion = productInfoDto.getPromotion();
        if (promotion.equals(NULL_PROMOTION)) {
            handleGeneralProductAdd(productInfoDto);
            return;
        }
        if (!promotionInventory.hasPromotion(promotion)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_CANNOT_FIND_PROMOTION);
        }
        handlePromotionProductAdd(productInfoDto);
    }

    public boolean hasProduct(String productName) {
        return productIndex.containsKey(productName);
    }

    public boolean hasPromotionProduct(String productName) {
        return promotionProductIndex.containsKey(productName);
    }

    public void isProductExist(String productName) {
        if (!hasProduct(productName)) {
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

    public void outTotalProduct(String productName, int totalQuantity) {
        if (hasPromotionProduct(productName)) {
            handlePromotionProductOut(productName, totalQuantity);
            return;
        }
        handleGeneralProductOut(productName, totalQuantity);
    }

    private void handlePromotionProductOut(String productName, int totalQuantity) {
        Product promotionProduct = findPromotionProduct(productName);
        Promotion promotion = promotionInventory.findPromotion(promotionProduct.getPromotion());
        if (promotion.isPromotionNow()) {
            int remainQuantity = outPromotionProduct(productName, totalQuantity);
            outProduct(productName, remainQuantity);
        }
    }

    private void handleGeneralProductOut(String productName, int totalQuantity) {
        int remainQuantity = outProduct(productName, totalQuantity);
        outPromotionProduct(productName, remainQuantity);
    }

    public int outProduct(String productName, int totalQuantity) {
        Product product = findProduct(productName);
        int currentProductQuantity = product.getQuantity();
        if (currentProductQuantity == 0) {
            return totalQuantity;
        }
        return subtractProductStock(product, totalQuantity, currentProductQuantity);
    }

    private int subtractProductStock(Product product, int totalQuantity, int currentProductQuantity) {
        if (currentProductQuantity < totalQuantity) {
            product.subStock(currentProductQuantity);
            return totalQuantity - currentProductQuantity;
        }
        product.subStock(totalQuantity);
        return 0;
    }

    public int outPromotionProduct(String productName, int totalQuantity) {
        if (!hasPromotionProduct(productName)) {
            return totalQuantity;
        }
        Product promotionProduct = findPromotionProduct(productName);
        int currentPromotionProductQuantity = promotionProduct.getQuantity();
        return subtractPromotionStock(promotionProduct, totalQuantity, currentPromotionProductQuantity);
    }

    private int subtractPromotionStock(Product promotionProduct, int totalQuantity,
                                       int currentPromotionProductQuantity) {
        if (currentPromotionProductQuantity - totalQuantity < 0) {
            promotionProduct.subStock(currentPromotionProductQuantity);
            return totalQuantity - currentPromotionProductQuantity;
        }
        promotionProduct.subStock(totalQuantity);
        return 0;
    }
}
