package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.user.model.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.runners.Parameterized;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name= "product_like")
public class ProductLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "productSizeIdx")
    private ProductSize productSize;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public ProductLike(Long idx, ProductSize productSize, User user, Status status) {
        this.idx = idx;
        this.productSize = productSize;
        this.user = user;
        this.status = status;
    }

    public ProductLike deleteProductLike(){
        this.status = Status.DELETED;
        return this;
    }

    public ProductLike activateProductLike(){
        this.status = Status.ACTIVATED;
        return this;
    }

}
