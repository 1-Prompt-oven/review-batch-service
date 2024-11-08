package com.promptoven.reviewBatchService.infrastructure;

import com.promptoven.reviewBatchService.domain.AggregateEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AggregateRepository extends JpaRepository<AggregateEntity, Long> {

    List<AggregateEntity> findAllByProductUuidIn(List<String> productUuids);
}
