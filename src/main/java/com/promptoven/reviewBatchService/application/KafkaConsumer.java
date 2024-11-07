package com.promptoven.reviewBatchService.application;

import com.promptoven.reviewBatchService.domain.ReviewBatchEntity;
import com.promptoven.reviewBatchService.dto.in.RequestMessageDto;
import com.promptoven.reviewBatchService.infrastructure.ReviewBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private static final String TOPIC = "review_event";
    private final ReviewBatchRepository reviewBatchRepository;

    @KafkaListener(topics = TOPIC, groupId = "kafka-review-service")
    public String consume(RequestMessageDto message) {

        ReviewBatchEntity reviewBatchEntity = message.toEntity();

        reviewBatchRepository.save(reviewBatchEntity);

        return "OK";
    }
}
