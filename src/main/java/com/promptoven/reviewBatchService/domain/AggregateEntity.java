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
@Table(name = "review_aggregate")
@Getter
public class AggregateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productUuid;

    @Column(nullable = false)
    private Long reviewCount;

    @Column(nullable = false)
    private Double avgStar;

    @Builder
    public AggregateEntity(Long id, String productUuid, Long reviewCount, Double avgStar) {
        this.id = id;
        this.productUuid = productUuid;
        this.reviewCount = reviewCount;
        this.avgStar = avgStar;
    }

    public AggregateEntity(String productUuid, Long reviewCount, Double avgStar) {
        this.productUuid = productUuid;
        this.reviewCount = reviewCount;
        this.avgStar = avgStar;
    }

    public AggregateEntity() {
    }
}
