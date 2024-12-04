package com.promptoven.reviewBatchService.infrastructure;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import com.promptoven.reviewBatchService.dto.out.SellerAggregateDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AggregateRepository extends JpaRepository<AggregateEntity, Long> {

    List<AggregateEntity> findAllByProductUuidIn(List<String> productUuids);

    Optional<AggregateEntity> findByProductUuid(String productUuid);

    @Query("SELECT new com.promptoven.reviewBatchService.dto.out.SellerAggregateDto(" +
            "r.sellerUuid, " +
            "AVG(r.avgStar)" +
            ") " +
            "FROM AggregateEntity r " +
            "GROUP BY r.sellerUuid")
    List<SellerAggregateDto> findSellerAggregate();
}
