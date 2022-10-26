package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name="product_image")
public class ProductImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name="productIdx")
    private Product product;

    @NotNull
    @Column(name = "image", columnDefinition = "varchar(1000)", nullable = false)
    private String image;

    @NotNull
    @Column(name = "position", columnDefinition = "tinyint", nullable = false)
    private byte position;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public ProductImage(Long idx, Product product, String image, byte position, Status status) {
        this.idx = idx;
        this.product = product;
        this.image = image;
        this.position = position;
        this.status = status;
    }
}
