package com.promptoven.reviewBatchService.presentation;

import com.promptoven.reviewBatchService.application.BatchSchedule;
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

    private final BatchSchedule batchSchedule;

    @GetMapping
    public String batch() throws Exception {
        batchSchedule.scheduleBatch();
        return "OK";
    }
}
