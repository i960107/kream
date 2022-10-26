package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Table(name="product_category_map")
public class ProductCategoryMap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name="productIdx")
    private Product product;

    @ManyToOne
    @JoinColumn(name="productCategoryDetailIdx")
    private ProductCategoryDetail productCategoryDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;


    @Builder
    public ProductCategoryMap(Long idx,
                              Product product,
                              ProductCategoryDetail productCategoryDetail,
                              Status status) {
        this.idx = idx;
        this.product = product;
        this.productCategoryDetail = productCategoryDetail;
        this.status = status;
    }
}

