package com.example.demo.src.style.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "hashtag")
public class HashTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name="name", columnDefinition = "varchar(20)", nullable = true)
    private String name;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
