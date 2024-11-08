package com.promptoven.reviewBatchService.dto.in;

import com.promptoven.reviewBatchService.domain.EventType;
import com.promptoven.reviewBatchService.domain.ReviewBatchEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
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

    public ReviewBatchEntity toEntity(EventType type) {
        return ReviewBatchEntity.builder()
                .productUuid(productUuid)
                .star(star)
                .type(type)
                .build();
    }
}