package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.bid.purchase.model.entity.BidPurchase;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name= "product_size")
public class ProductSize extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @Where(clause = "status = 'ACTIVATED'")
    @JoinColumn(name="productIdx")
    private Product product;

    @NotBlank
    @Column(name="size", columnDefinition = "varchar(10)", nullable = false)
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "productSize")
    @Where(clause = "status = 'ACTIVATED'")
    List<ProductLike> productLikes = new ArrayList<>();

    public ProductSize(Long idx, Product product, String size, Status status) {
        this.idx = idx;
        this.product = product;
        this.size = size;
        this.status = status;
    }
}
