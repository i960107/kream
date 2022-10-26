package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
@Table(name="brand")
public class Brand extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name="name", columnDefinition = "varchar(45)",nullable = false)
    private String name;

    @Column(name="image", columnDefinition = "varchar(1000)",nullable = true)
    private String image;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition = "varchar(10)",nullable = false)
    private Status status;

    @Builder
    public Brand(Long idx, String name, String image, Status status) {
        this.idx = idx;
        this.name = name;
        this.image = image;
        this.status = status;
    }
}

