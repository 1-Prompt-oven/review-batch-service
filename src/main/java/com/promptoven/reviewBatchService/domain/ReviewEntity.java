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
@Table(name = "review")
@Getter
@NoArgsConstructor
public class ReviewEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String productUuid;

    @Column(nullable = false)
    private String memberUuid;

    @Column(nullable = false)
    private int star;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public ReviewEntity(Long reviewId, String productUuid, String memberUuid, int star, String contents,
            Boolean isDeleted) {
        this.reviewId = reviewId;
        this.productUuid = productUuid;
        this.memberUuid = memberUuid;
        this.star = star;
        this.contents = contents;
        this.isDeleted = isDeleted;
    }
}
