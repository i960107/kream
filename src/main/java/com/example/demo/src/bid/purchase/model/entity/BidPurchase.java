package com.example.demo.src.bid.purchase.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import com.example.demo.src.product.model.entity.ProductSize;
import com.example.demo.config.BidPrice;
import com.example.demo.src.user.model.entity.Address;
import com.example.demo.utils.ValidationRegex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bid_purchase")
public class BidPurchase extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "userIdx", columnDefinition = "int unsigned", nullable = false)
    private Long userIdx;

    @Column(name = "orderNum", columnDefinition = "varchar(20)", nullable = true)
    private String orderNum;

    @ManyToOne
    @JoinColumn(name = "productSizeIdx")
    private ProductSize productSize;

    @NotNull
    @BidPrice
    @Column(name = "bidPrice", columnDefinition = "int unsigned", nullable = false)
    private Long bidPrice;

    @NotNull
    @FutureOrPresent
    @Column(name = "deadline", columnDefinition = "date", nullable = false)
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "addressIdx")
    private Address address;

    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    @Column(name = "point", columnDefinition = "varchar(10)", nullable = false)
    private String point;

    @NotNull
    @PositiveOrZero
    @Column(name = "inspectionFee", columnDefinition = "int unsigned default 0", nullable = false)
    private Long inspectionFee;

    @NotNull
    @PositiveOrZero
    @Column(name = "shippingFee", columnDefinition = "int unsigned default 0", nullable = false)
    private Long shippingFee;

    @NotNull
    @PositiveOrZero
    @Column(name = "totalPrice", columnDefinition = "int unsigned", nullable = false)
    private Long totalPrice;

    @PositiveOrZero
    @Column(name = "cardIdx", columnDefinition = "int unsigned", nullable = true)
    private Long cardIdx;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BidPurchase(Long idx,
                       Long userIdx,
                       String orderNum,
                       ProductSize productSize,
                       Long bidPrice,
                       LocalDate deadline,
                       Address address,
                       String point,
                       Long inspectionFee,
                       Long shippingFee,
                       Long totalPrice,
                       Long cardIdx,
                       Status status) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.orderNum = orderNum;
        this.productSize = productSize;
        this.bidPrice = bidPrice;
        this.deadline = deadline;
        this.address = address;
        this.point = point;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.cardIdx = cardIdx;
        this.status = status;
    }



    public BidPurchase registerCardAndUpdateStatus(Long cardIdx,Status status){
        this.cardIdx = cardIdx;
        this.status = status;
        return this;
    }
    public BidPurchase updateOrderNo(String orderNo){
        this.orderNum = orderNo;
        return this;
    }
    public BidPurchase updateStatus(Status status){
        this.status = status;
        return this;
    }
}
