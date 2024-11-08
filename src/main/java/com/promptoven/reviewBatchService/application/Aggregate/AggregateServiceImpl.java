package com.promptoven.reviewBatchService.application.Aggregate;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.dto.out.AggregateResponseDto;
import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AggregateServiceImpl implements AggregateService {

    private final AggregateRepository aggregateRepository;

    @Override
    public AggregateResponseDto getAggregateData(String productUuid) {

        AggregateEntity aggregateEntity = aggregateRepository.findByProductUuid(productUuid)
                .orElseThrow(() -> new IllegalArgumentException("productUuid not found"));

        return AggregateResponseDto.toDto(aggregateEntity);
    }
}
