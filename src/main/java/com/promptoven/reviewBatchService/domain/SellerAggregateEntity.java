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

@Table(name = "seller_aggregate")
@Entity
@Getter
@NoArgsConstructor
public class SellerAggregateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sellerUuid;

    @Column(nullable = false)
    private Double avgStar;

    @Builder
    public SellerAggregateEntity(Long id, String sellerUuid, Double avgStar) {
        this.id = id;
        this.sellerUuid = sellerUuid;
        this.avgStar = avgStar;
    }
}
