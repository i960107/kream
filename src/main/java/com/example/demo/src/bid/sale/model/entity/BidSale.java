package com.example.demo.src.bid.sale.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.BidPrice;
import com.example.demo.config.Status;
import com.example.demo.src.product.model.entity.ProductSize;
import com.example.demo.src.user.model.entity.Account;
import com.example.demo.src.user.model.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bid_sale")
public class BidSale extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "userIdx", columnDefinition = "int unsigned", nullable = false)
    private Long userIdx;

    @ManyToOne
    @JoinColumn(name = "productSizeIdx", referencedColumnName = "idx")
    private ProductSize productSize;

    @Column(name = "bidPrice", columnDefinition = "int unsigned", nullable = false)
    @BidPrice
    private Long bidPrice;

    @Column(name = "orderNum", columnDefinition = "varchar(20)", nullable = true)
    private String orderNum;

    @NotNull
    @FutureOrPresent
    @Column(name = "deadline", columnDefinition = "date", nullable = false)
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "addressIdx")
    private Address address;

    @NotNull
    @PositiveOrZero
    @Column(name = "inspectionFee", columnDefinition = "int unsigned default 0", nullable = false)
    private Long inspectionFee;

    @NotNull
    @PositiveOrZero
    @Column(name = "commission", columnDefinition = "int unsigned default 0", nullable = false)
    private Long commission;

    @NotNull
    @PositiveOrZero
    @Column(name = "shippingFee", columnDefinition = "int unsigned default 0", nullable = false)
    private Long shippingFee;

    @NotNull
    @PositiveOrZero
    @Column(name = "settlementAmount", columnDefinition = "int unsigned", nullable = false)
    private Long settlementAmount;

    @ManyToOne
    @JoinColumn(name = "accountIdx")
    private Account account;

    @PositiveOrZero
    @Column(name = "cardIdx", columnDefinition = "int unsigned", nullable = true)
    private Long cardIdx;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BidSale(
            Long idx,
            Long userIdx,
            ProductSize productSize,
            Long bidPrice,
            String orderNum,
            LocalDate deadline,
            Address address,
            Long inspectionFee,
            Long commission,
            Long shippingFee,
            Long settlementAmount,
            Account account,
            Long paymentCardIdx,
            Status status) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.productSize = productSize;
        this.bidPrice = bidPrice;
        this.orderNum = orderNum;
        this.deadline = deadline;
        this.address = address;
        this.inspectionFee = inspectionFee;
        this.commission = commission;
        this.shippingFee = shippingFee;
        this.settlementAmount = settlementAmount;
        this.account = account;
        this.cardIdx = paymentCardIdx;
        this.status = status;
    }

    public BidSale updateStatus(Status status) {
        this.status = status;
        return this;
    }

    public BidSale updateCardIdxAndModelNo(Long paymentCardIdx, String orderNum) {
        this.cardIdx = paymentCardIdx;
        this.orderNum = orderNum;
        return this;
    }
    public BidSale updateOrderNum(String orderNum){
        this.orderNum = orderNum;
        return this;
    }

}
