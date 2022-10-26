package com.example.demo.src.product.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.style.model.entity.Style;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "brandIdx")
    private Brand brand;

    @NotNull
    @Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", columnDefinition = "varchar(100)", nullable = false)
    private String description;

    @NotNull
    @Column(name = "thumbnail", columnDefinition = "varchar(1000)", nullable = false)
    private String thumbnail;


    @NotNull
    @Column(name = "luxury", columnDefinition = "varchar(5) default FALSE", nullable = false)
    private String luxury;

    @NotNull
    @Column(name = "modelNo", columnDefinition = "varchar(45)", nullable = false)
    private String modelNo;

    @NotNull
    @Column(name = "releasePrice", columnDefinition = "int unsigned", nullable = false)
    private Long releasePrice;

    @NotNull
    @Column(name = "releaseDate", columnDefinition = "date", nullable = false)
    private LocalDate releaseDate;

    @NotNull
    @Column(name = "color", columnDefinition = "varchar(45)", nullable = false)
    private String color;

    @NotNull
    @Column(name = "gender", columnDefinition = "varchar(10)", nullable = false)
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "product")
    List<ProductCategoryMap> categoryList = new ArrayList<>();


    @OneToMany(mappedBy = "product")
    @Where(clause = "status = 'ACTIVATED'")
    List<ProductSize> productSizes = new ArrayList<>();


    @OneToMany(mappedBy = "product")
    @OrderBy("position asc")
    @Where(clause = "status = 'ACTIVATED'")
    List<ProductImage> productImages = new ArrayList<>();

    @Builder
    public Product(Long idx, Brand brand, String name, String description, String thumbnail, String luxury, String modelNo, Long releasePrice, LocalDate releaseDate, String color, String gender, Status status) {
        this.idx = idx;
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.luxury = luxury;
        this.modelNo = modelNo;
        this.releasePrice = releasePrice;
        this.releaseDate = releaseDate;
        this.color = color;
        this.gender = gender;
        this.status = status;
    }
}
