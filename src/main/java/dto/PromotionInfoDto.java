package dto;

import java.time.LocalDateTime;

public class PromotionInfoDto {
    String name;
    int buyCount;
    int getCount;
    LocalDateTime startAt;
    LocalDateTime endAt;

    public PromotionInfoDto(String name, int buyCount, int getCount, LocalDateTime startAt, LocalDateTime endAt) {
        this.name = name;
        this.buyCount = buyCount;
        this.getCount = getCount;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public String getName() {
        return name;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getGetCount() {
        return getCount;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }
}
