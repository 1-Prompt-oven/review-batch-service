package com.promptoven.reviewBatchService.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_batch")
@Getter
@NoArgsConstructor
public class ReviewBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @Column(nullable = false)
    private String productUuid;

    @Column(nullable = false)
    private int star;

    @Builder
    public ReviewBatchEntity(Long batchId, String productUuid, int star) {
        this.batchId = batchId;
        this.productUuid = productUuid;
        this.star = star;
    }
}
