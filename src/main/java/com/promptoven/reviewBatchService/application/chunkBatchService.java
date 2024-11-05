//package com.promptoven.reviewBatchService.application;
//
//import com.promptoven.reviewBatchService.domain.AggregateEntity;
//import com.promptoven.reviewBatchService.dto.AggregateDto;
//import com.promptoven.reviewBatchService.infrastructure.AggregateRepository;
//import com.promptoven.reviewBatchService.infrastructure.ReviewRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.data.RepositoryItemReader;
//import org.springframework.batch.item.data.RepositoryItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@RequiredArgsConstructor
//public class chunkBatchService {
//
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager; // 청크가 진행되다 실패되었을 때 다시 처리 할 수 있도록 해줌
//    private final AggregateRepository aggregateRepository;
//    private final ReviewRepository reviewRepository;
//
//    @Bean
//    public Job job() {
//        return new JobBuilder("job", jobRepository)
//                .start(step())
//                .build();
//    }
//
//    @Bean
//    public Step step() {
//        return new StepBuilder("step", jobRepository)
//                .<List<AggregateDto>, List<AggregateEntity>>chunk(10, transactionManager)
//                .reader(readEntity()) // review에서 데이터 가져오기
//                .processor(processor()) // 집계 처리하기
//                .writer(writer()) // 집계 데이터 삽입하기
//                .build();
//    }
//
//    @Bean
//    public RepositoryItemReader<List<AggregateDto>> readEntity() {
//        RepositoryItemReader<List<AggregateDto>> reader = new RepositoryItemReader<>();
//        reader.setRepository(reviewRepository);
//        reader.setMethodName("aggregateReviewData"); // 전체 리뷰 데이터를 읽어옴
//        return reader;
//    }
//
//    @Bean
//    public ItemProcessor<List<AggregateDto>, List<AggregateEntity>> processor() {
//        return new ItemProcessor<List<AggregateDto>, List<AggregateEntity>>() {
//
//            @Override
//            public List<AggregateEntity> process(@NonNull List<AggregateDto> aggregateDtoList) throws Exception {
//
//                List<String> productUuidList = aggregateDtoList.stream()
//                        .map(AggregateDto::getProductUuid)
//                        .toList();
//
//                List<AggregateEntity> existEntityList = aggregateRepository.findAllByProductUuidIn(productUuidList);
//
//                Map<String, AggregateEntity> existingEntityMap = existEntityList.stream()
//                        .collect(Collectors.toMap(AggregateEntity::getProductUuid, entity -> entity));
//
//                List<AggregateEntity> toSaveData = new ArrayList<>();
//
//                for (AggregateDto aggregateDto : aggregateDtoList) {
//                    AggregateEntity existingEntity = existingEntityMap.get(aggregateDto.getProductUuid());
//                    if (existingEntity != null) {
//                        existingEntity = AggregateEntity.builder()
//                                .id(existingEntity.getId())
//                                .productUuid(existingEntity.getProductUuid())
//                                .avgStar(aggregateDto.getAvgStar())
//                                .reviewCount(aggregateDto.getReviewCount())
//                                .build();
//                    } else {
//                        existingEntity = aggregateDto.toEntity(aggregateDto);
//                    }
//
//                    toSaveData.add(existingEntity);
//                }
//
//                return toSaveData;
//            }
//        };
//    }
//
//    @Bean
//    public RepositoryItemWriter<List<AggregateEntity>> writer() {
//        return new CustomAggregateEntityWriter(aggregateRepository);
//    }
//
//    // Custom ItemWriter 구현
//    @Component
//    public static class CustomAggregateEntityWriter extends RepositoryItemWriter<List<AggregateEntity>> {
//
//        private final AggregateRepository aggregateRepository;
//
//        public CustomAggregateEntityWriter(AggregateRepository aggregateRepository) {
//            this.aggregateRepository = aggregateRepository;
//        }
//
//        @Override
//        public void write(Chunk<? extends List<AggregateEntity>> chunk) throws Exception {
//            for (List<AggregateEntity> itemList : chunk.getItems()) {
//                aggregateRepository.saveAll(itemList); // List<AggregateEntity> 한 번에 저장
//            }
//        }
//    }
//
//}
