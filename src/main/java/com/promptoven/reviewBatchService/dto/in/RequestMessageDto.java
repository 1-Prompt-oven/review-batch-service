package com.promptoven.reviewBatchService.dto.in;

import com.promptoven.reviewBatchService.domain.ReviewBatchEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestMessageDto {

    private String productUuid;
    private int star;

    @Builder
    public RequestMessageDto(String productUuid, int star) {
        this.productUuid = productUuid;
        this.star = star;
    }

    public ReviewBatchEntity toEntity() {
        return ReviewBatchEntity.builder()
                .productUuid(productUuid)
                .star(star)
                .build();
    }
}
