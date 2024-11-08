package com.promptoven.reviewBatchService.application.Aggregate;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.dto.out.AggregateResponseDto;
import com.promptoven.reviewBatchService.global.error.BaseException;
import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AggregateServiceImpl implements AggregateService {

    private final AggregateRepository aggregateRepository;

    @Override
    public AggregateResponseDto getAggregateData(String productUuid) {

        Optional<AggregateEntity> aggregateEntity = aggregateRepository.findByProductUuid(productUuid);

        return aggregateEntity
                .map(AggregateResponseDto::toDto)
                .orElseGet(() -> new AggregateResponseDto(
                        productUuid, 0.0, 0L
                ));
    }
}

