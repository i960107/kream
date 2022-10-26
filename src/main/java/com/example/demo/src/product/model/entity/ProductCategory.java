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
@Table(name = "product_category")
public class ProductCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private byte idx;

    @NotNull
    @Column(name = "name", columnDefinition = "varchar(10)", nullable = false)
    private String name;

    @Column(name = "position", columnDefinition = "smallint", nullable = true)
    private byte position;

    @Enumerated(EnumType.STRING)
    @Column(name = "status ", columnDefinition = "varchar(10) default 'ACTIVATED", nullable = false)
    private Status status;

    @Builder
    public ProductCategory(
            byte idx,
            String name,
            byte position,
            Status status) {
        this.idx = idx;
        this.name = name;
        this.position = position;
        this.status = status;
    }
}
