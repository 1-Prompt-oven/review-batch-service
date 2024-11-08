package com.promptoven.reviewBatchService.application.Aggregate;

import com.promptoven.reviewBatchService.dto.out.AggregateResponseDto;

public interface AggregateService {

    AggregateResponseDto getAggregateData(String productUuid);
}
