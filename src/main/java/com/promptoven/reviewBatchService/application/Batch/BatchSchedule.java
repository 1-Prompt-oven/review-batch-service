package com.promptoven.reviewBatchService.application.Batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 15 * * *") // UTC 기준 15시에 실행 (KST 0시)
    public void scheduleBatch() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 고유 파라미터
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("aggregateJob"), jobParameters);
    }
}
