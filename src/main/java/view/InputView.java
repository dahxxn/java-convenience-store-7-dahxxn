package view;

import camp.nextstep.edu.missionutils.Console;
import constant.FileMetadata;
import constant.PurchaseValidation;
import error.CustomException;
import error.ExceptionMessage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import model.ProductInventory;

public class InputView {
    private static final String SINGLE_PURCHASE_INFO_REGEX = PurchaseValidation.SINGLE_PURCHASE_INFO_REGEX.toString();
    private static final String MULTIPLE_PURCHASE_INFO_REGEX = PurchaseValidation.MULTIPLE_PURCHASE_INFO_REGEX.toString();
    private static final String PURCHASE_INFO_DELIMITER = FileMetadata.DELIMITER.toString();
    private static final String YES = PurchaseValidation.YES.toString();
    private static final String NO = PurchaseValidation.NO.toString();

    OutputView outputView = new OutputView();
    ProductInventory productInventory;

    public InputView(ProductInventory productInventory) {
        this.productInventory = productInventory;
    }

    public String readInput(String message) {
        outputView.printMessage(message);
        return Console.readLine();
    }

    public String validateInputNotEmpty(String inputValue) {
        if (inputValue == null) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_WRONG);
        }
        inputValue = inputValue.trim();
        if (inputValue.isEmpty()) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_WRONG);
        }
        return inputValue;
    }

    public LinkedHashMap<String, Integer> readPurchaseInfo(String guideMessage) {
        while (true) {
            try {
                outputView.printLineBreak();
                String inputPurchaseInfo = readInput(guideMessage);
                inputPurchaseInfo = validateInputNotEmpty(inputPurchaseInfo);
                validateInputPurchaseInfoFormat(inputPurchaseInfo);
                return extractPurchaseInfoToMap(inputPurchaseInfo);
            } catch (IllegalArgumentException e) {
                outputView.printMessage(e.getMessage());
            }
        }
    }

    public void validateInputPurchaseInfoFormat(String inputPurchaseInfo) {
        if (!inputPurchaseInfo.matches(MULTIPLE_PURCHASE_INFO_REGEX)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_PURCHASE_INFO_IS_NOT_VALID);
        }
    }

    public LinkedHashMap<String, Integer> extractPurchaseInfoToMap(String inputPurchaseInfo) {
        List<String> inputPurchases = delimiterInputPurchaseInfo(inputPurchaseInfo);
        LinkedHashMap<String, Integer> inputPurchaseInfoMap = new LinkedHashMap<>();
        for (String inputPurchase : inputPurchases) {
            String productName = extractPurchaseName(inputPurchase);
            int productQuantity =
                    extractQuantity(inputPurchase) + isDuplicatedProduct(productName, inputPurchaseInfoMap);
            productInventory.isProductExist(productName);
            productInventory.isCanBuyProduct(productName, productQuantity);
            inputPurchaseInfoMap.put(productName, productQuantity);
        }
        return inputPurchaseInfoMap;
    }

    public int isDuplicatedProduct(String productName, HashMap<String, Integer> inputPurchaseInfoMap) {
        if (inputPurchaseInfoMap.containsKey(productName)) {
            return inputPurchaseInfoMap.get(productName);
        }
        return 0;
    }

    public String extractPurchaseName(String inputPurchase) {
        return inputPurchase.replaceAll(SINGLE_PURCHASE_INFO_REGEX, "$1");
    }

    public int extractQuantity(String inputPurchase) {
        return Integer.parseInt(inputPurchase.replaceAll(SINGLE_PURCHASE_INFO_REGEX, "$2"));
    }

    public List<String> delimiterInputPurchaseInfo(String inputPurchaseInfo) {
        return List.of(inputPurchaseInfo.split(PURCHASE_INFO_DELIMITER));
    }

    public boolean readYesOrNoInfo(String guideMessage) {
        while (true) {
            try {
                outputView.printLineBreak();
                String inputYesOrNoInfo = readInput(guideMessage);
                inputYesOrNoInfo = validateInputNotEmpty(inputYesOrNoInfo);
                return validateInputYesOrNoInfo(inputYesOrNoInfo);
            } catch (IllegalArgumentException e) {
                outputView.printLineBreak();
                outputView.printMessage(e.getMessage());
            }
        }
    }

    public boolean validateInputYesOrNoInfo(String inputYesOrNoInfo) {
        if (YES.equals(inputYesOrNoInfo)) {
            return true;
        }
        if (NO.equals(inputYesOrNoInfo)) {
            return false;
        }
        throw new CustomException(ExceptionMessage.ERROR_MESSAGE_INPUT_IS_WRONG);
    }


}