package com.promptoven.reviewBatchService.vo.out;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AggregateResponseVo {
    private String productUuid;
    private double avgStar;
    private long reviewCount;

    @Builder
    public AggregateResponseVo(String productUuid, double avgStar, long reviewCount) {
        this.productUuid = productUuid;
        this.avgStar = avgStar;
        this.reviewCount = reviewCount;
    }
}
