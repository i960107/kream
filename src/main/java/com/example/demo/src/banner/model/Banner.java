package com.example.demo.src.banner.model;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "banner")
public class Banner extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "location", columnDefinition = "varchar(10) ", nullable = false)
    private String location;

    @Column(name = "image", columnDefinition = "varchar(1000) ", nullable = false)
    private String image;

    @Column(name = "position", columnDefinition = "tinyint", nullable = false)
    private byte position;

    @Column(name = "productIdx", columnDefinition = "int unsigned", nullable = true)
    private Long productIdx;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public Banner(Long idx, String location, String image, byte position, Long productIdx, Status status) {
        this.idx = idx;
        this.location = location;
        this.image = image;
        this.position = position;
        this.productIdx = productIdx;
        this.status = status;
    }
}
