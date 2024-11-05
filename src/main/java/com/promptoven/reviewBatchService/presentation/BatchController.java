package com.promptoven.reviewBatchService.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
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
        JobParameters jobParameters = new JobParameters();

        jobLauncher.run(jobRegistry.getJob("aggregateJob"), jobParameters);

        return "OK";
    }
}
