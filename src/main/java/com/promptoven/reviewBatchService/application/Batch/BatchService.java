package com.promptoven.reviewBatchService.application.Batch;

import static com.promptoven.reviewBatchService.global.common.response.BaseResponseStatus.NO_EXIST_EVENT;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.domain.EventType;
import com.promptoven.reviewBatchService.dto.out.AggregateDto;
import com.promptoven.reviewBatchService.global.error.BaseException;
import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
import com.promptoven.reviewBatchService.infrastructure.ReviewBatchRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class BatchService {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AggregateRepository aggregateRepository;
    private final ReviewBatchRepository reviewBatchRepository;

    @Bean
    public Job aggregateJob() {
        return new JobBuilder("aggregateJob", jobRepository)
                .start(createStep())
                .next(updateStep())
                .next(deleteStep())
                .build();
    }

    @Bean
    public Step createStep() {
        return new StepBuilder("createStep", jobRepository)
                .tasklet(createTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step updateStep() {
        return new StepBuilder("updateStep", jobRepository)
                .tasklet(updateTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step deleteStep() {
        return new StepBuilder("deleteStep", jobRepository)
                .tasklet(deleteTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet createTasklet() {
        return (contribution, chunkContext) -> processTasklet(EventType.CREATE);
    }

    @Bean
    public Tasklet updateTasklet() {
        return (contribution, chunkContext) -> processTasklet(EventType.UPDATE);
    }

    @Bean
    public Tasklet deleteTasklet() {
        return (contribution, chunkContext) -> processTasklet(EventType.DELETE);
    }

    private RepeatStatus processTasklet(EventType eventType) {

        List<AggregateDto> aggregateDtoList = reviewBatchRepository.findAggregatedByType(eventType);

        Map<String, AggregateEntity> existingEntityMap = loadExistingEntities(aggregateDtoList);

        List<AggregateEntity> toSaveData = aggregateDtoList.stream()
                .map(aggregateDto -> {

                    AggregateEntity existingEntity = existingEntityMap.get(aggregateDto.getProductUuid());

                    if (eventType == EventType.CREATE) {
                        if (existingEntity != null) {
                            return updateEntity(existingEntity, aggregateDto, EventType.CREATE);
                        } else {
                            return aggregateDto.toEntity(aggregateDto);
                        }
                    } else if (eventType == EventType.UPDATE) {
                        if (existingEntity != null) {
                            return updateEntity(existingEntity, aggregateDto, EventType.UPDATE);
                        }
                    } else if (eventType == EventType.DELETE) {
                        if (existingEntity != null && existingEntity.getReviewCount() > aggregateDto.getReviewCount()) {
                            return updateEntity(existingEntity, aggregateDto, EventType.DELETE);
                        } else if (existingEntity != null) {
                            aggregateRepository.delete(existingEntity);
                            return null;
                        }
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        aggregateRepository.saveAll(toSaveData);

        if (eventType == EventType.DELETE) {
            reviewBatchRepository.deleteAll();
        }

        return RepeatStatus.FINISHED;
    }

    private Map<String, AggregateEntity> loadExistingEntities(List<AggregateDto> aggregateDtoList) {
        List<String> productUuidList = aggregateDtoList.stream()
                .map(AggregateDto::getProductUuid)
                .toList();

        return aggregateRepository.findAllByProductUuidIn(productUuidList).stream()
                .collect(Collectors.toMap(AggregateEntity::getProductUuid, entity -> entity));
    }

    private AggregateEntity updateEntity(AggregateEntity existingEntity, AggregateDto aggregateDto,
            EventType eventType) {
        long updatedReviewCount;
        double updatedAvgStar;

        switch (eventType) {

            case CREATE:
                updatedReviewCount = existingEntity.getReviewCount() + aggregateDto.getReviewCount();
                updatedAvgStar = (existingEntity.getAvgStar() * existingEntity.getReviewCount()
                        + aggregateDto.getAvgStar() * aggregateDto.getReviewCount()) / updatedReviewCount;
                break;

            case UPDATE:
                updatedReviewCount = existingEntity.getReviewCount();
                double totalStars = existingEntity.getAvgStar() * existingEntity.getReviewCount();
                totalStars = totalStars - aggregateDto.getPreviousTotalStar() + aggregateDto.getNewTotalStar();
                updatedAvgStar = totalStars / existingEntity.getReviewCount();
                break;

            case DELETE:
                updatedReviewCount = existingEntity.getReviewCount() - aggregateDto.getReviewCount();
                if (updatedReviewCount > 0) {
                    updatedAvgStar = (existingEntity.getAvgStar() * existingEntity.getReviewCount()
                            - aggregateDto.getAvgStar() * aggregateDto.getReviewCount()) / updatedReviewCount;
                } else {
                    updatedAvgStar = 0.0;
                }
                break;

            default:
                throw new BaseException(NO_EXIST_EVENT);
        }

        return AggregateEntity.builder()
                .id(existingEntity.getId())
                .productUuid(existingEntity.getProductUuid())
                .reviewCount(updatedReviewCount)
                .avgStar(updatedAvgStar)
                .build();
    }
}