package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@Table(name ="product_category_detail")
public class ProductCategoryDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "name", columnDefinition = "varchar(10)", nullable = false)
    private String name;

    @NotNull
    @Column(name = "exposure", columnDefinition = "varchar(10) default TRUE", nullable = false)
    private String exposure;

    @Column(name = "image", columnDefinition = "varchar(1000) ", nullable = true)
    private String image;

    @ManyToOne
    @JoinColumn(name="productCategoryIdx")
    private ProductCategory productCategory;

    @Column(name = "position", columnDefinition = "tinyint", nullable = true)
    private byte position;

    @Column(name = "description", columnDefinition = "varchar(20)", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="status ",columnDefinition = "varchar(10) default 'ACTIVATED",nullable= false)
    private Status status;


    @Builder
    public ProductCategoryDetail(
            Long idx,
            String name,
            String exposure,
            String image,
            ProductCategory productCategory,
            byte position,
            String description,
            Status status) {
        this.idx = idx;
        this.name = name;
        this.exposure = exposure;
        this.image = image;
        this.productCategory = productCategory;
        this.position = position;
        this.description = description;
        this.status = status;
    }
}
