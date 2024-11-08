package com.promptoven.reviewBatchService.dto.out;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.vo.out.AggregateResponseVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AggregateResponseDto {

    private String productUuid;
    private double avgStar;
    private long reviewCount;

    @Builder
    public AggregateResponseDto(String productUuid, double avgStar, long reviewCount) {
        this.productUuid = productUuid;
        this.avgStar = avgStar;
        this.reviewCount = reviewCount;
    }

    public static AggregateResponseDto toDto(AggregateEntity aggregateEntity) {
        return AggregateResponseDto.builder()
                .productUuid(aggregateEntity.getProductUuid())
                .avgStar(aggregateEntity.getAvgStar())
                .reviewCount(aggregateEntity.getReviewCount())
                .build();
    }

    public static AggregateResponseVo toVo(AggregateResponseDto aggregateResponseDto) {
        return AggregateResponseVo.builder()
                .productUuid(aggregateResponseDto.getProductUuid())
                .avgStar(aggregateResponseDto.getAvgStar())
                .reviewCount(aggregateResponseDto.getReviewCount())
                .build();
    }
}
