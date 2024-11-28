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


}
