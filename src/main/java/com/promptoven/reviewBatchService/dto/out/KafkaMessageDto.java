package com.promptoven.reviewBatchService.dto.out;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessageDto {

    private Map<String, Double> productAggregateMap;
    private Map<String, Double> sellerAggregateMap;
}
