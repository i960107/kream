package com.example.demo.src.style.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.user.model.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "style")
public class Style extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @Where(clause = "status = 'ACTIVATED'")
    @OrderBy(value = "position asc")
    @OneToMany(mappedBy = "style")
    private List<StyleImage> styleImages;

    @Column(name="content", columnDefinition = "varchar(500)", nullable = true)
    private String content;

    @Column(name="viewCount", columnDefinition = "int unsigned default 0 ", nullable = false)
    private Long viewCount;

    @NotNull
    @Column(name = "status", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Style(Long idx, User user, List<StyleImage> styleImages, String content, Long viewCount, Status status) {
        this.idx = idx;
        this.user = user;
        this.styleImages = styleImages;
        this.content = content;
        this.viewCount = viewCount;
        this.status = status;
    }

    public Style addViewCount(){
        this.viewCount +=1;
        return this;
    }
}
