package com.promptoven.reviewBatchService.infrastructure;

import com.promptoven.reviewBatchService.domain.SellerAggregateEntity;
import com.promptoven.reviewBatchService.dto.out.SellerAggregateDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SellerAggregateRepository extends JpaRepository<SellerAggregateEntity, Long> {

    @Query("SELECT new com.promptoven.reviewBatchService.dto.out.SellerAggregateDto(" +
            "r.sellerUuid, " +
            "AVG(r.avgStar)" +
            ") " +
            "FROM AggregateEntity r " +
            "GROUP BY r.sellerUuid")
    List<SellerAggregateDto> findSellerAggregate();
}
