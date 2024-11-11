package model;

import Util.FileReaderUtil;
import constant.FileMetadata;
import dto.PromotionInfoDto;
import error.CustomException;
import error.ExceptionMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PromotionInventory {
    private static HashMap<String, Integer> promotionIndex;
    private static final String PROMOTION_MD_FILE_PATH = FileMetadata.PROMOTION_MD_FILE_PATH.toString();
    private final List<Promotion> promotions;

    public PromotionInventory() {
        promotions = new ArrayList<>();
        promotionIndex = new HashMap<>();
        setPromotionInventory();
    }

    public LocalDateTime convertStringToLocalDateTime(String dateInfo) {
        LocalDate yearMonthDay = LocalDate.parse(dateInfo, DateTimeFormatter.ISO_DATE);
        return yearMonthDay.atStartOfDay();
    }

    public void setPromotionInfo(List<String> promotionInfo) {
        PromotionInfoDto promotionInfoDto = new PromotionInfoDto(
                promotionInfo.get(PromotionFileStructure.PROMOTION_NAME.ordinal()),
                Integer.parseInt(promotionInfo.get(PromotionFileStructure.PROMOTION_BUY_COUNT.ordinal())),
                Integer.parseInt(promotionInfo.get(PromotionFileStructure.PROMOTION_GET_COUNT.ordinal())),
                convertStringToLocalDateTime(promotionInfo.get(PromotionFileStructure.PROMOTION_START_AT.ordinal())),
                convertStringToLocalDateTime(promotionInfo.get(PromotionFileStructure.PROMOTION_END_AT.ordinal()))
        );
        addPromotion(promotionInfoDto);
    }

    public void setPromotionInventory() {
        List<String> promotionInfoLines = FileReaderUtil.readFile(PROMOTION_MD_FILE_PATH);
        for (int i = 1; i < promotionInfoLines.size(); i++) {
            String promotionInfoLine = promotionInfoLines.get(i);
            List<String> promotionInfo = FileReaderUtil.delimiterLine(promotionInfoLine);
            setPromotionInfo(promotionInfo);
        }
    }

    public void addPromotion(PromotionInfoDto promotionInfoDto) {
        String promotionName = promotionInfoDto.getName();
        if (hasPromotion(promotionName)) {
            throw new CustomException(ExceptionMessage.ERROR_MESSAGE_ALREADY_HAVE_PROMOTION);
        }
        Promotion promotion = new Promotion(promotionInfoDto);
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
