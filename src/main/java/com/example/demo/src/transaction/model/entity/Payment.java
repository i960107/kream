package com.example.demo.src.transaction.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.user.model.entity.User;
import com.example.demo.utils.ValidationRegex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="payment")
@Getter
@NoArgsConstructor
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @NotNull
    @Column(name = "transactionIdx", columnDefinition = "int unsigned", nullable = false)
    private Long transactionIdx;

    @NotNull
    @Column(name = "cardIdx", columnDefinition = "int unsigned", nullable = false)
    private Long cardIdx;

    @NotNull
    @Column(name = "totalPrice", columnDefinition = "int unsigned", nullable = false)
    private Long totalPrice;

    @NotNull
    @Column(name = "pointUsed", columnDefinition = "int unsigned", nullable = false)
    private Long pointUsed;

    @NotNull
    @Column(name = "paymentAmount", columnDefinition = "int unsigned", nullable = false)
    private Long paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public Payment(Long idx, User user, Long transactionIdx, Long cardIdx, Long totalPrice, Long pointUsed, Long paymentAmount, Status status) {
        this.idx = idx;
        this.user = user;
        this.transactionIdx = transactionIdx;
        this.cardIdx = cardIdx;
        this.totalPrice = totalPrice;
        this.pointUsed = pointUsed;
        this.paymentAmount = paymentAmount;
        this.status = status;
    }
}
