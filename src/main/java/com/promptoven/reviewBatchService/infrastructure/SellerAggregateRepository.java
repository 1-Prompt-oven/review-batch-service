package com.promptoven.reviewBatchService.infrastructure;

import com.promptoven.reviewBatchService.domain.SellerAggregateEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerAggregateRepository extends JpaRepository<SellerAggregateEntity, Long> {

    Optional<SellerAggregateEntity> findBySellerUuid(String sellerUuid);
}
