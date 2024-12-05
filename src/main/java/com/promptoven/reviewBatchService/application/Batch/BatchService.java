package com.promptoven.reviewBatchService.application.Batch;

import static com.promptoven.reviewBatchService.global.common.response.BaseResponseStatus.NO_EXIST_EVENT;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.domain.EventType;
import com.promptoven.reviewBatchService.domain.SellerAggregateEntity;
import com.promptoven.reviewBatchService.dto.out.AggregateDto;
import com.promptoven.reviewBatchService.dto.out.KafkaMessageDto;
import com.promptoven.reviewBatchService.dto.out.SellerAggregateDto;
import com.promptoven.reviewBatchService.global.error.BaseException;
import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
import com.promptoven.reviewBatchService.infrastructure.ReviewBatchRepository;
import com.promptoven.reviewBatchService.infrastructure.SellerAggregateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SellerAggregateRepository sellerAggregateRepository;
    private final AggregateRepository aggregateRepository;
    private final ReviewBatchRepository reviewBatchRepository;

    @Bean
    public Job aggregateJob() {
        return new JobBuilder("aggregateJob", jobRepository)
                .start(createStep())
                .next(updateStep())
                .next(deleteStep())
                .next(sellerAggregateStep())
                .next(sendToKafkaStep())
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
    public Step sellerAggregateStep() {
        return new StepBuilder("sellerAggregateStep", jobRepository)
                .tasklet(sellerAggregateTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step sendToKafkaStep() {
        return new StepBuilder("sendToKafkaStep", jobRepository)
                .tasklet(sendToKafkaTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet sellerAggregateTasklet() {
        return (contribution, chunkContext) -> {
            processSellerAggregate();
            return RepeatStatus.FINISHED;
        };
    }

    private void processSellerAggregate() {
        List<SellerAggregateDto> sellerAggregateDtoList = aggregateRepository.findSellerAggregate();

        for (SellerAggregateDto dto : sellerAggregateDtoList) {
            Optional<SellerAggregateEntity> existingEntity = sellerAggregateRepository.findBySellerUuid(
                    dto.getSellerUuid());

            if (existingEntity.isPresent()) {
                SellerAggregateEntity updatedEntity = SellerAggregateEntity.builder()
                        .id(existingEntity.get().getId())
                        .sellerUuid(existingEntity.get().getSellerUuid())
                        .avgStar(dto.getAvgStar())
                        .build();
                sellerAggregateRepository.save(updatedEntity);
                saveSellerData(existingEntity.get().getSellerUuid(), dto.getAvgStar());
            } else {
                SellerAggregateEntity newEntity = SellerAggregateEntity.builder()
                        .sellerUuid(dto.getSellerUuid())
                        .avgStar(dto.getAvgStar())
                        .build();
                sellerAggregateRepository.save(newEntity);
                saveSellerData(newEntity.getSellerUuid(), newEntity.getAvgStar());
            }
        }
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

    @Bean
    Tasklet sendToKafkaTasklet() {
        return (contribution, chunkContext) -> {
            sendAllDataToKafka();
            return RepeatStatus.FINISHED;
        };
    }

    private RepeatStatus processTasklet(EventType eventType) {

        List<AggregateDto> aggregateDtoList = reviewBatchRepository.findAggregatedByType(eventType);

        Map<String, AggregateEntity> existingEntityMap = loadExistingEntities(aggregateDtoList);

        List<AggregateEntity> toSaveData = aggregateDtoList.stream()
                .map(aggregateDto -> {

                    AggregateEntity existingEntity = existingEntityMap.get(aggregateDto.getProductUuid());

                    AggregateEntity updatedEntity = null;
                    if (eventType == EventType.CREATE) {
                        if (existingEntity != null) {
                            updatedEntity = updateEntity(existingEntity, aggregateDto, EventType.CREATE);
                        } else {
                            updatedEntity = aggregateDto.toEntity(aggregateDto);
                        }
                    } else if (eventType == EventType.UPDATE) {
                        if (existingEntity != null) {
                            updatedEntity = updateEntity(existingEntity, aggregateDto, EventType.UPDATE);
                        }
                    } else if (eventType == EventType.DELETE) {

                        if (existingEntity != null && existingEntity.getReviewCount() > aggregateDto.getReviewCount()) {
                            updatedEntity = updateEntity(existingEntity, aggregateDto, EventType.DELETE);
                        } else if (existingEntity != null) {
                            aggregateRepository.delete(existingEntity);
                            return null;
                        }
                    }

                    if (updatedEntity != null) {
                        saveProductData(updatedEntity.getProductUuid(), updatedEntity.getAvgStar());
                    }

                    return updatedEntity;
                })
                .filter(Objects::nonNull)
                .toList();

        // message -> productUuid, avgStar
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
                double totalStars = existingEntity.getAvgStar() * updatedReviewCount;
                totalStars = totalStars - aggregateDto.getPreviousTotalStar() + aggregateDto.getNewTotalStar();
                updatedAvgStar = totalStars / updatedReviewCount;
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
                .sellerUuid(existingEntity.getSellerUuid())
                .reviewCount(updatedReviewCount)
                .avgStar(updatedAvgStar)
                .build();
    }

    private void sendAllDataToKafka() {
        Map<String, Double> allModifiedProductData = getAllProductData();
        Map<String, Double> allModifiedSellerData = getAllSellerData();
        
        try {
            KafkaMessageDto kafkaMessageDto = KafkaMessageDto.builder()
                    .productAggregateMap(allModifiedProductData)
                    .sellerAggregateMap(allModifiedSellerData)
                    .build();

            kafkaTemplate.send("${aggregate-finish-event}", kafkaMessageDto);
            log.info("Sent KafkaMessageDto to Kafka: {}", kafkaMessageDto);

        } catch (Exception e) {
            log.error("Error sending aggregate data to Kafka: {}", e.getMessage());
        } finally {
            clearAllData();
        }
    }

    private final Map<String, Double> productAvgStarMap = new ConcurrentHashMap<>();
    private final Map<String, Double> sellerAvgStarMap = new ConcurrentHashMap<>();

    private void saveProductData(String productUuid, double productAvgStar) {
        productAvgStarMap.put(productUuid, productAvgStar);
    }

    private void saveSellerData(String sellerUuid, double sellerAvgStar) {
        sellerAvgStarMap.put(sellerUuid, sellerAvgStar);
    }

    private Map<String, Double> getAllProductData() {
        return new HashMap<>(productAvgStarMap);
    }

    private Map<String, Double> getAllSellerData() {
        return new HashMap<>(sellerAvgStarMap);
    }

    private void clearAllData() {
        productAvgStarMap.clear();
        sellerAvgStarMap.clear();
    }

}