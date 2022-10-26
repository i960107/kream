package com.example.demo.src.style.model;

import com.example.demo.config.Auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Role;
import com.example.demo.src.style.StyleProvider;
import com.example.demo.src.style.StyleService;
import com.example.demo.src.style.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StyleController {
    private final StyleProvider styleProvider;
    private final StyleService styleService;

    @GetMapping("/api/styles")
    public BaseResponse<GetStyleListRes> retrieveStyleList(
            @PageableDefault(size = 30, page = 0, sort = "viewCount", direction = Sort.Direction.DESC) Pageable pageable
    ) throws BaseException {
        GetStyleListRes responseDto = styleProvider.retrieveStyleList(pageable);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/api/styles/{styleIdx}")
    public BaseResponse<String> addViewCount(
            @PathVariable("styleIdx") Long styleIdx
    ) throws BaseException {
        styleService.addViewCount(styleIdx);
        return new BaseResponse<String>("조회수 증가됨");
    }

    @GetMapping("api/styles/{styleIdx}/comments")
    public BaseResponse<List<GetStyleCommentRes>> retrieveStyleComments(
            @PathVariable("styleIdx") Long styleIdx
    ) throws BaseException {
        List<GetStyleCommentRes> responseDto = styleProvider.retrieveStyleComments(styleIdx);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("api/styles/hash-tag")
    public BaseResponse<GetStyleListRes> retrieveStyleByHashTag(
            @RequestParam(required = true) String hashTag,
            @PageableDefault(size = 30, page = 0, sort = "style.viewCount", direction = Sort.Direction.DESC) Pageable pageable
    ) throws BaseException {
        GetStyleListRes responseDto = styleProvider.retrieveStyleByHashTag(hashTag, pageable);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("api/styles/{styleIdx}/likes")
    public BaseResponse<List<GetStyleLikeRes>> retrieveStyleLikes(
            @PathVariable("styleIdx") Long styleIdx
    ) throws BaseException {
        List<GetStyleLikeRes> responseDto = styleProvider.retrieveStyleLikes(styleIdx);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("api/styles/{styleIdx}/comment")
    public BaseResponse<PostStyleCommentRes> createStyleComment(
            @PathVariable("styleIdx") Long styleIdx,
            @RequestAttribute("userIdx") Long userIdx,
            @Valid @RequestBody PostStyleCommentReq styleCommentReq
    ) throws BaseException {
        PostStyleCommentRes responseDto = styleService.createStyleComment(userIdx, styleIdx, styleCommentReq);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("api/styles/{styleIdx}/like")
    public BaseResponse<PostStyleLikeRes> createStyleLike(
            @PathVariable("styleIdx") Long styleIdx,
            @RequestAttribute("userIdx") Long userIdx
    ) throws BaseException {
        PostStyleLikeRes responseDto = styleService.createStyleLike(userIdx, styleIdx);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @DeleteMapping("api/styles/{styleIdx}/like")
    public BaseResponse<PostStyleLikeRes> deleteStyleLike(
            @PathVariable("styleIdx") Long styleIdx,
            @RequestAttribute("userIdx") Long userIdx
    ) throws BaseException {
        PostStyleLikeRes responseDto = styleService.deleteStyleLike(userIdx, styleIdx);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping(value = "api/styles", consumes = {"multipart/form-data"})
    public BaseResponse<PostStyleRes> createStyle(
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "products", required = false) List<Long> products,
            @RequestAttribute("userIdx") Long userIdx
    ) throws BaseException {
        Long uploadedStyleIdx = styleService.createStyle(userIdx, images, content, products);
        PostStyleRes  responseDto= styleProvider.retrieveStyle(uploadedStyleIdx);
        return new BaseResponse<>(responseDto);
    }
}
