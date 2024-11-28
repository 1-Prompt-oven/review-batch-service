package com.promptoven.reviewBatchService.dto.out;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AggregateDto {

    private String productUuid;
    private String sellerUuid;
    private Long reviewCount;
    private Double avgStar;
    private double previousTotalStar;
    private double newTotalStar;

    @Builder
    public AggregateDto(String productUuid, String sellerUuid, Long reviewCount, Double avgStar, double previousTotalStar,
            double newTotalStar) {
        this.productUuid = productUuid;
        this.sellerUuid = sellerUuid;
        this.reviewCount = reviewCount;
        this.avgStar = avgStar;
        this.previousTotalStar = previousTotalStar;
        this.newTotalStar = newTotalStar;
    }

    public AggregateEntity toEntity(AggregateDto aggregateDto) {
        return AggregateEntity.builder()
                .productUuid(aggregateDto.getProductUuid())
                .sellerUuid(aggregateDto.getSellerUuid())
                .reviewCount(aggregateDto.getReviewCount())
                .avgStar(aggregateDto.getAvgStar())
                .build();
    }
}
