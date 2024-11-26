package com.promptoven.reviewBatchService.application.Kafka;

import com.promptoven.reviewBatchService.domain.EventType;
import com.promptoven.reviewBatchService.domain.ReviewBatchEntity;
import com.promptoven.reviewBatchService.dto.in.RequestMessageDto;
import com.promptoven.reviewBatchService.infrastructure.ReviewBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private static final String GROUP_ID = "kafka-review-batch-service";
    private final ReviewBatchRepository reviewBatchRepository;

    @KafkaListener(topics = "${review-create-event}", groupId = GROUP_ID)
    public void consumeCreate(RequestMessageDto message) {
        consumeEvent(message, EventType.CREATE);
    }

    @KafkaListener(topics = "${review-update-event}", groupId = GROUP_ID)
    public void consumeUpdate(RequestMessageDto message) {
        consumeEvent(message, EventType.UPDATE);
    }

    @KafkaListener(topics = "${review-delete-event}", groupId = GROUP_ID)
    public void consumeDelete(RequestMessageDto message) {
        consumeEvent(message, EventType.DELETE);
    }

    private void consumeEvent(RequestMessageDto message, EventType eventType) {
        ReviewBatchEntity reviewBatchEntity = message.toEntity(eventType);
        reviewBatchRepository.save(reviewBatchEntity);
    }
}
