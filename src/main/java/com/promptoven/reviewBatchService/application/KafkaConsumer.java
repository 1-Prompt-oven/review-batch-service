package com.promptoven.reviewBatchService.application;

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

    private static final String CREATE_TOPIC = "create_review_event"; // 각각 이벤트의 발행
    private static final String UPDATE_TOPIC = "update_review_event";
    private static final String DELETE_TOPIC = "delete_review_event";
    private final ReviewBatchRepository reviewBatchRepository;

    @KafkaListener(topics = CREATE_TOPIC, groupId = "kafka-review-service")
    public String consumeCreate(RequestMessageDto message) {

        ReviewBatchEntity reviewBatchEntity = message.toEntity(EventType.CREATE);

        reviewBatchRepository.save(reviewBatchEntity);

        return "OK";
    }

    @KafkaListener(topics = UPDATE_TOPIC, groupId = "kafka-review-service")
    public String consumeUpdate(RequestMessageDto message) {

        System.out.println("delete data : " + message.toString());

        ReviewBatchEntity reviewBatchEntity = message.toEntity(EventType.UPDATE);

        reviewBatchRepository.save(reviewBatchEntity);

        return "OK";
    }

    @KafkaListener(topics = DELETE_TOPIC, groupId = "kafka-review-service")
    public String consumeDelete(RequestMessageDto message) {

        System.out.println("delete data : " + message.toString());

        ReviewBatchEntity reviewBatchEntity = message.toEntity(EventType.DELETE);

        reviewBatchRepository.save(reviewBatchEntity);

        return "OK";
    }
}
