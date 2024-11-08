package com.promptoven.reviewBatchService.infrastructure;

import com.promptoven.reviewBatchService.domain.EventType;
import com.promptoven.reviewBatchService.domain.ReviewBatchEntity;
import com.promptoven.reviewBatchService.dto.out.AggregateDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewBatchRepository extends JpaRepository<ReviewBatchEntity, Long> {

    @Query("SELECT new com.promptoven.reviewBatchService.dto.out.AggregateDto(" +
            "r.productUuid, " +
            "CAST(COUNT(r) AS long), " +
            "CAST(AVG(r.star) AS double), " +
            "SUM(CASE WHEN r.type = 'UPDATE' THEN r.previousStar ELSE 0 END), " + // previousTotalStar
            "SUM(CASE WHEN r.type = 'UPDATE' THEN r.star ELSE 0 END) " +       // newTotalStar
            ") " +
            "FROM ReviewBatchEntity r " +
            "WHERE r.type = :type " +
            "GROUP BY r.productUuid")
    List<AggregateDto> findAggregatedByType(@Param("type") EventType type);



}
