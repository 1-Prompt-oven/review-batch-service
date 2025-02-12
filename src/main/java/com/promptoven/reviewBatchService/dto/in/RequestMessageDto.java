package com.promptoven.reviewBatchService.dto.in;

import com.promptoven.reviewBatchService.domain.EventType;
import com.promptoven.reviewBatchService.domain.ReviewBatchEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestMessageDto {

    private String productUuid;
    private String sellerUuid;
    private int star;
    private int previousStar;

    @Builder
    public RequestMessageDto(String productUuid, String sellerUuid, int star, int previousStar) {
        this.productUuid = productUuid;
        this.sellerUuid = sellerUuid;
        this.star = star;
        this.previousStar = previousStar;
    }

    public ReviewBatchEntity toEntity(EventType type) {
        return ReviewBatchEntity.builder()
                .productUuid(productUuid)
                .sellerUuid(sellerUuid)
                .star(star)
                .previousStar(previousStar)
                .type(type)
                .build();
    }
}
