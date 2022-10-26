package com.example.demo.src.style.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "style_hashtag")
public class StyleHashTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name ="styleIdx")
    private Style style;


    @Column(name="hashtagIdx",nullable = false, columnDefinition = "int unsigned")
    private Long hashtagIdx;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
