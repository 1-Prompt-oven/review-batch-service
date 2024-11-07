package com.promptoven.reviewBatchService.presentation;

import com.promptoven.reviewBatchService.application.BatchSchedule;
import lombok.RequiredArgsConstructor;
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
