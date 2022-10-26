package com.example.demo.src.user.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_own")
public class UserOwn extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "productSizeIdx", columnDefinition = "int unsigned", nullable = false)
    private Long productSizeIdx;

    @Column(name = "userIdx", columnDefinition = "int unsigned", nullable = false)
    private Long userIdx;

    @Column(name = "buyPrice", columnDefinition = "int unsigned", nullable = false)
    private Long buyPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public UserOwn(Long idx, Long productSizeIdx, Long userIdx, Long buyPrice, Status status) {
        this.idx = idx;
        this.productSizeIdx = productSizeIdx;
        this.userIdx = userIdx;
        this.buyPrice = buyPrice;
        this.status = status;
    }
}
