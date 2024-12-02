package com.promptoven.reviewBatchService.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerAggregateDto {

    private String sellerUuid;
    private double avgStar;
}
