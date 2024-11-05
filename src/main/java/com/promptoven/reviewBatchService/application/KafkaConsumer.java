package com.promptoven.reviewBatchService.application;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private static final String TOPIC = "review_event";
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;


    @KafkaListener(topics = TOPIC, groupId = "kafka-review-service")
    public String consume(String message) throws Exception{

         System.out.println("Consumed message: " + message);

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 매번 다른 파라미터 추가
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("aggregateJob"), jobParameters);

        return "OK";
    }
}
