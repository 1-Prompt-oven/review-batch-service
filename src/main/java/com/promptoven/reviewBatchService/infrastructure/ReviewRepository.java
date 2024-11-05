package com.promptoven.reviewBatchService.infrastructure;

import com.promptoven.reviewBatchService.domain.ReviewEntity;
import com.promptoven.reviewBatchService.dto.AggregateDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT new com.promptoven.reviewBatchService.dto.AggregateDto(p.productUuid, " +
            "COALESCE(CAST(COUNT(r) AS long), 0)," +
            "COALESCE(CAST(AVG(r.star) AS double), 0.0)) " +
            "FROM AggregateEntity p LEFT JOIN ReviewEntity r ON p.productUuid = r.productUuid AND r.isDeleted = false " +
            "GROUP BY p.productUuid")
    List<AggregateDto> aggregateReviewData();
}
