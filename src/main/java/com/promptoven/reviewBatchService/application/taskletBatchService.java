package com.promptoven.reviewBatchService.application;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.dto.AggregateDto;
import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
import com.promptoven.reviewBatchService.infrastructure.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class taskletBatchService {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AggregateRepository aggregateRepository;
    private final ReviewRepository reviewRepository;

    @Bean
    public Job job() {

        return new JobBuilder("aggregateJob", jobRepository)
                .start(aggregateStep())
                .build();
    }

    @Bean
    public Step aggregateStep() {

        return new StepBuilder("aggregateStep", jobRepository)
                .tasklet(aggregateTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet aggregateTasklet() {

        return (contribution, chunkContext) -> {

            List<AggregateDto> aggregateDtoList = reviewRepository.aggregateReviewData();

            List<String> productUuidList = aggregateDtoList.stream()
                    .map(AggregateDto::getProductUuid)
                    .toList();

            List<AggregateEntity> existEntityList = aggregateRepository.findAllByProductUuidIn(productUuidList);

            Map<String, AggregateEntity> existingEntityMap = existEntityList.stream()
                    .collect(Collectors.toMap(AggregateEntity::getProductUuid, entity -> entity));

            List<AggregateEntity> toSaveData = new ArrayList<>();

            for (AggregateDto aggregateDto : aggregateDtoList) {
                AggregateEntity existingEntity = existingEntityMap.get(aggregateDto.getProductUuid());
                if (existingEntity != null) {
                    existingEntity = AggregateEntity.builder()
                            .id(existingEntity.getId())
                            .productUuid(existingEntity.getProductUuid())
                            .avgStar(aggregateDto.getAvgStar())
                            .reviewCount(aggregateDto.getReviewCount())
                            .build();
                } else {
                    existingEntity = aggregateDto.toEntity(aggregateDto);
                }
                toSaveData.add(existingEntity);
            }

            aggregateRepository.saveAll(toSaveData);

            return RepeatStatus.FINISHED;
        };
    }
}
