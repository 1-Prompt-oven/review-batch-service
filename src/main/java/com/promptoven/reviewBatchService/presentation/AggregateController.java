package com.promptoven.reviewBatchService.presentation;

import com.promptoven.reviewBatchService.application.Aggregate.AggregateService;
import com.promptoven.reviewBatchService.application.Batch.BatchSchedule;
import com.promptoven.reviewBatchService.dto.out.AggregateResponseDto;
import com.promptoven.reviewBatchService.vo.out.AggregateResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/review/aggregate")
@RequiredArgsConstructor
public class AggregateController {

    private final AggregateService aggregateService;
    private final BatchSchedule batchSchedule;

    @GetMapping("/testSchedule")
    public String batch() throws Exception {
        batchSchedule.scheduleBatch();
        return "OK";
    }

    @GetMapping({"/{productUuid}"})
    public AggregateResponseVo getAggregateData(@PathVariable String productUuid) {

        AggregateResponseDto aggregateResponseDto = aggregateService.getAggregateData(productUuid);

        return AggregateResponseDto.toVo(aggregateResponseDto);
    }
}
