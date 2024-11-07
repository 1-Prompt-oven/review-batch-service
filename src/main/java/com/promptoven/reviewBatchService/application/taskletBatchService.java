package com.promptoven.reviewBatchService.application;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.dto.out.AggregateDto;
import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
import com.promptoven.reviewBatchService.infrastructure.ReviewBatchRepository;
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
    private final ReviewBatchRepository reviewBatchRepository;

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

            List<AggregateDto> aggregateDtoList = reviewBatchRepository.aggregateReviewData();

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
                    // 기존 데이터가 있는 경우, 리뷰 개수와 평균 별점을 재계산
                    long updatedReviewCount = existingEntity.getReviewCount() + aggregateDto.getReviewCount();
                    double updatedAvgStar =
                            (existingEntity.getAvgStar() * existingEntity.getReviewCount()
                                    + aggregateDto.getAvgStar() * aggregateDto.getReviewCount())
                                    / updatedReviewCount;
                    // (existingEntity.getAvgStar() * existingEntity.getReviewCount()): 기존 데이터의 총 별점 구하기
                    // (aggregateDto.getAvgStar() * aggregateDto.getReviewCount()): 새로운 데이터의 총 별점 구하기
                    // 두 값을 더한 후, 총 리뷰 개수로 나누어 평균 별점 구하기
                    // (기존 데이터의 총 별점 + 새로운 데이터의 총 별점) / 총 리뷰 개수
                    // 즉, 기존 데이터와 새로운 데이터를 합산하여 새로운 평균 별점을 계산
                    // 이후, 갱신된 리뷰 개수와 평균 별점으로 엔티티를 갱신

                    // 갱신된 데이터를 반영하여 새로운 엔티티 생성
                    existingEntity = AggregateEntity.builder()
                            .id(existingEntity.getId())
                            .productUuid(existingEntity.getProductUuid())
                            .reviewCount(updatedReviewCount)
                            .avgStar(updatedAvgStar)
                            .build();
                } else {
                    // 기존 데이터가 없는 경우, 새로운 엔티티로 변환하여 추가
                    existingEntity = aggregateDto.toEntity(aggregateDto);
                }
                toSaveData.add(existingEntity);
            }

            // 알간 수집 데이터 삭제
            reviewBatchRepository.deleteAll();

            // 집계 데이터 저장
            aggregateRepository.saveAll(toSaveData);

            return RepeatStatus.FINISHED;
        };
    }
}
