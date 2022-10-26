package com.example.demo.src.transaction.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.product.model.entity.ProductSize;
import com.example.demo.src.product.model.entity.ProductSizeRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name= "transaction")
public class Transaction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "bidSaleIdx", columnDefinition = "int unsigned", nullable = false)
    private Long bidSaleIdx;

    @NotNull
    @Column(name = "bidPurchaseIdx", columnDefinition = "int unsigned", nullable = false)
    private Long bidPurchaseIdx;

    @ManyToOne
    @JoinColumn(name= "productSizeIdx")
    private ProductSize productSize;

    @NotNull
    @Column(name = "winningBidPrice", columnDefinition = "int unsigned", nullable = false)
    private Long winningBidPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'BEFORE_PAYMENT'", nullable = false)
    private Status status;

    @Builder
    public Transaction(Long idx,
                       Long bidSaleIdx,
                       Long bidPurchaseIdx,
                       ProductSize productSize,
                       Long winningBidPrice,
                       Status status) {
        this.idx = idx;
        this.bidSaleIdx = bidSaleIdx;
        this.bidPurchaseIdx = bidPurchaseIdx;
        this.productSize = productSize;
        this.winningBidPrice = winningBidPrice;
        this.status = status;
    }

    public Transaction updateStatus(Status status){
        this.status = status;
        return this;
    }
}
