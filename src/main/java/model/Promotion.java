package model;

import camp.nextstep.edu.missionutils.DateTimes;
import dto.PromotionInfoDto;
import java.time.LocalDateTime;

public class Promotion {
    private final String name;
    private final int buyCount;
    private final int getCount;
    private final int totalCount;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    Promotion(PromotionInfoDto promotionInfoDto) {
        this.name = promotionInfoDto.getName();
        this.buyCount = promotionInfoDto.getBuyCount();
        this.getCount = promotionInfoDto.getGetCount();
        this.startAt = promotionInfoDto.getStartAt();
        this.endAt = promotionInfoDto.getEndAt();
        this.totalCount = buyCount + getCount;
    }

    public int getGetCount() {
        return getCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isPromotionNow() {
        LocalDateTime now = DateTimes.now();
        if (startAt.isBefore(now) && endAt.isAfter(now)) {
            return true;
        }
        return false;
    }

    public int checkPromotionPossible(int buyCount, int promotionStock) {
        int buySetCount = buyCount / totalCount;
        int promotionStockSetCount = promotionStock / totalCount;
        if (buySetCount > promotionStockSetCount) {
            return promotionStockSetCount * totalCount - buyCount;
        }
        return buySetCount;
    }

    public int calculateRemainingCount(int setCount, int initialCount) {
        return initialCount - totalCount * setCount;
    }

    public boolean checkGetMorePromotion(int remainBuyCount, int promotionStock) {
        if (remainBuyCount == buyCount && promotionStock >= totalCount) {
            return true;
        }
        return false;
    }
}
