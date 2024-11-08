package model;

import Util.FileReaderUtil;
import error.CustomException;
import error.ExceptionMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PromotionInventory {
    private final List<Promotion> promotions;
    private static HashMap<String, Integer> promotionIndex;
    static final String PROMOTION_MD_FILE_PATH = "src/main/resources/promotions.md";

    public PromotionInventory() {
        promotions = new ArrayList<>();
        promotionIndex = new HashMap<>();
        setPromotionInventory();
    }

    public void setPromotionInfo(List<String> promotionInfo) {
        String name = promotionInfo.get(PromotionInfoLineStructure.PROMOTION_NAME.ordinal());
        int buyCount = Integer.parseInt(promotionInfo.get(PromotionInfoLineStructure.PROMOTION_BUY_COUNT.ordinal()));
        int getCount = Integer.parseInt(promotionInfo.get(PromotionInfoLineStructure.PROMOTION_GET_COUNT.ordinal()));
        LocalDate startAt = LocalDate.parse(
                promotionInfo.get(PromotionInfoLineStructure.PROMOTION_START_AT.ordinal()), DateTimeFormatter.ISO_DATE);
        LocalDate endAt = LocalDate.parse(
                promotionInfo.get(PromotionInfoLineStructure.PROMOTION_END_AT.ordinal()), DateTimeFormatter.ISO_DATE);
        addPromotion(name, buyCount, getCount, startAt.atStartOfDay(), endAt.atStartOfDay());
    }

    public void setPromotionInventory() {
        List<String> promotionInfoLines = FileReaderUtil.readFile(PROMOTION_MD_FILE_PATH);

        for (int i = 1; i < promotionInfoLines.size(); i++) {
            String promotionInfoLine = promotionInfoLines.get(i);
            List<String> promotionInfo = FileReaderUtil.delimiterLine(promotionInfoLine);
            setPromotionInfo(promotionInfo);
        }
    }

    public void addPromotion(String promotionName, int buyCount, int getCount, LocalDateTime startAt,
                             LocalDateTime endAt) {
        if (hasPromotion(promotionName)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_ALREADY_HAVE_PROMOTION);
        }
        Promotion promotion = new Promotion(promotionName, buyCount, getCount, startAt, endAt);
        promotions.add(promotion);
        promotionIndex.put(promotionName, promotions.size() - 1);
    }

    public Promotion findPromotion(String promotionName) {
        if (!hasPromotion(promotionName)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_CANNOT_FIND_PROMOTION);
        }
        int index = promotionIndex.get(promotionName);
        return promotions.get(index);
    }

    public boolean hasPromotion(String promotionName) {
        return promotionIndex.containsKey(promotionName);
    }

}
