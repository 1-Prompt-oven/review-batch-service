package com.promptoven.reviewBatchService.dto;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AggregateDto {

    private String productUuid;
    private Long reviewCount;
    private Double avgStar;

    @Builder
    public AggregateDto(String productUuid, Long reviewCount, Double avgStar) {
        this.productUuid = productUuid;
        this.reviewCount = reviewCount;
        this.avgStar = avgStar;
    }

    public AggregateEntity toEntity(AggregateDto aggregateDto) {
        return AggregateEntity.builder()
                .productUuid(aggregateDto.getProductUuid())
                .reviewCount(aggregateDto.getReviewCount())
                .avgStar(aggregateDto.getAvgStar())
                .build();
    }
}
