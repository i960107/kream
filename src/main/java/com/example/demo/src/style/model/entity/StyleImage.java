package com.example.demo.src.style.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "style_image")
public class StyleImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "styleIdx")
    private Style style;

    @NotBlank
    @Column(name="image", columnDefinition = "varchar(1000)", nullable = false)
    private String image;

    @NotNull
    @Column(name="position", columnDefinition = "tinyint", nullable = false)
    private Byte position;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public StyleImage(Long idx, Style style, String image, Byte position, Status status) {
        this.idx = idx;
        this.style = style;
        this.image = image;
        this.position = position;
        this.status = status;
    }
}
