package com.example.demo.src.user.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.user.model.dto.PatchUserReq;
import com.example.demo.utils.ValidationRegex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Email
    @Column(nullable = false, columnDefinition = "varchar(45)")
    private String email;

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(500)")
    private String password;

    @Column(nullable = true, name = "sneakersSize", columnDefinition = "smallint(6)")
    private Integer sneakersSize;

    @Pattern(regexp = ValidationRegex.regexPhone)
    @Column(nullable = false, columnDefinition = "varchar(13)")
    private String phone;

    @Column(nullable = true, columnDefinition = "varchar(1000)")
    private String profileImage;

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(45)")
    private String name;

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(10)")
    private String nickName;

    @Column(nullable = true, columnDefinition = "varchar(100)")
    private String introduction;

    @Column(nullable = false, columnDefinition = "varchar(10) default '일반 회원' ")
    private String grade;

    @Column(nullable = false, columnDefinition = "int(10) unsigned default 0")
    private Long point;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(10) default 'ACTIVATED'")
    private Status status;

    @Builder
    public User(Long idx,
                String email,
                String password,
                Integer sneakersSize,
                String phone,
                String profileImage,
                String name,
                String nickName,
                String introduction,
                String grade,
                Long point,
                Status status) {
        this.idx = idx;
        this.email = email;
        this.password = password;
        this.sneakersSize = sneakersSize;
        this.phone = phone;
        this.profileImage = profileImage;
        this.name = name;
        this.nickName = nickName;
        this.introduction = introduction;
        this.grade = grade;
        this.point = point;
        this.status = status;
    }



    public User updateTempPw(String tempPw){
       this.password = tempPw;
       return this;
    }

    public User usePoint(Long point){
        this.point -=point;
        return this;
    }

    public User update(String name,String nickName,String introduction){
        if(name != null){
            this.name = name;
        }
        if(nickName != null){
            this.nickName = nickName;
        }
        if(introduction !=null){
            this.introduction = introduction;
        }
        return this;
    }
    public User updateProfileImage(String profileImage){
        this.profileImage = profileImage;
        return this;
    }
}

