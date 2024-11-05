package com.promptoven.reviewBatchService.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping
    public String batch() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 매번 다른 파라미터 추가
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("aggregateJob"), jobParameters);

        return "OK";
    }
}
