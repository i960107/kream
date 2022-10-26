package com.example.demo.src.user.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.utils.ValidationRegex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@Entity
@Table(name="account")
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "userIdx", columnDefinition = "int unsigned", nullable = false)
    private Long userIdx;

    @NotBlank
    @Column(name = "bank", columnDefinition = "varchar(10)", nullable = false)
    private String bank;

    @NotBlank
    @Column(name = "account", columnDefinition = "varchar(45)", nullable = false)
    private String account;

    @NotBlank
    @Column(name = "accountHolder", columnDefinition = "varchar(45)", nullable = false)
    private String accountHolder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public Account(Long idx, Long userIdx, String bank, String account, String accountHolder, Status status) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
        this.status = status;
    }

    public Account delete(){
        this.status = Status.DELETED;
        return this;
    }
}
