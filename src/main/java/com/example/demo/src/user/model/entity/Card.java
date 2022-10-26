package com.example.demo.src.user.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.utils.ValidationRegex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card")
public class Card extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "userIdx", columnDefinition = "int unsigned", nullable = false)
    private Long userIdx;

    @NotBlank
    @Column(name = "company", columnDefinition = "varchar(10)", nullable = false)
    private String company;

    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBin)
    @Column(name = "bin", columnDefinition = "varchar(20)", nullable = false)
    private String bin;

    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    @Column(name = "defaultCard", columnDefinition = "varchar(5)", nullable = false)
    private String defaultCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public Card(Long idx, Long userIdx, String company, String bin, String defaultCard, Status status) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.company = company;
        this.bin = bin;
        this.defaultCard = defaultCard;
        this.status = status;
    }

    public Card updateAsDefaultCard(){
        this.defaultCard = "TRUE";
        return this;
    }
    public Card deleteFromDefaultCard(){
        this.defaultCard = "FALSE";
        return this;
    }
}
