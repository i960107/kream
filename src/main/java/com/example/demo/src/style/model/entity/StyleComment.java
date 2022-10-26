package com.example.demo.src.style.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.user.model.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "style_comment")
public class StyleComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name="userIdx")
    private User user;

    @NotNull
    @Column(name="styleIdx", columnDefinition = "int unsigned", nullable = false)
    private Long styleIdx;

    @Column(name="content", columnDefinition = "varchar(100)", nullable = true)
    private String content;

    @Column(name= "parentIdx", columnDefinition = "int unsigned", nullable = false)
    private Long parentIdx;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public StyleComment(Long idx, User user, Long styleIdx, String content, Long parentIdx, Status status) {
        this.idx = idx;
        this.user = user;
        this.styleIdx = styleIdx;
        this.content = content;
        this.parentIdx = parentIdx;
        this.status = status;
    }
}

