package com.example.demo.src.style.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetStyleCommentRes {
    private Long idx;
    private Long userIdx;
    private String nickName;
    private String profileImage;
    private String content;
    private LocalDateTime createdAt;
    private List<GetStyleChildCommentRes> childCommentList;

    public GetStyleCommentRes(
            Long idx,
            Long userIdx,
            String nickName,
            String profileImage,
            String content,
            LocalDateTime createdAt,
            List<GetStyleChildCommentRes> childCommentList) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.content = content;
        this.createdAt = createdAt;
        this.childCommentList = childCommentList;
    }
}
