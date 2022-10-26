package com.example.demo.src.style;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.product.model.ProductProvider;
import com.example.demo.src.product.model.entity.Product;
import com.example.demo.src.style.model.dto.*;
import com.example.demo.src.style.model.entity.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.entity.User;
import com.example.demo.utils.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class StyleService {
    private final StyleRepository styleRepository;
    private final UserProvider userProvider;
    private final StyleCommentRepository styleCommentRepository;
    private final StyleLikeRepository styleLikeRepository;
    private final S3Service s3Service;
    private final StyleImageRepository styleImageRepository;
    private final StyleProductRepository styleProductRepository;
    private final ProductProvider productProvider;

    public void addViewCount(Long styleIdx) throws BaseException {
        Style style = styleRepository.findByIdxAndStatus(styleIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STYLE_WRONG_IDX));
        styleRepository.save(style.addViewCount());
    }

    public PostStyleCommentRes createStyleComment(
            Long userIdx,
            Long styleIdx,
            PostStyleCommentReq styleCommentReq
    ) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        styleRepository.findByIdxAndStatus(styleIdx, Status.ACTIVATED);
        if (styleCommentReq.getParentIdx() != null) {
            styleCommentRepository.findByIdxAndStatus(styleCommentReq.getParentIdx(), Status.ACTIVATED)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.STYLE_COMMENT_WRONG_IDX));
        }
        StyleComment comment = styleCommentRepository.save(
                StyleComment.builder()
                        .user(user)
                        .styleIdx(styleIdx)
                        .content(styleCommentReq.getContent())
                        .parentIdx(styleCommentReq.getParentIdx())
                        .status(Status.ACTIVATED)
                        .build()
        );
        return new PostStyleCommentRes(
                comment.getStyleIdx(),
                comment.getIdx(),
                comment.getUser().getIdx(),
                comment.getUser().getNickName(),
                comment.getUser().getProfileImage(),
                comment.getParentIdx(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    public PostStyleLikeRes createStyleLike(Long userIdx, Long styleIdx) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        styleRepository.findByIdxAndStatus(styleIdx, Status.ACTIVATED);
        Optional<StyleLike> originalStyleLike = styleLikeRepository.findByUserIdxAndStyleIdx(userIdx, styleIdx);
        StyleLike styleLike;

        if (originalStyleLike.isPresent()) {
            if (originalStyleLike.get().getStatus() == Status.ACTIVATED)
                throw new BaseException(BaseResponseStatus.STYLE_LIKE_ALREADY_EXIST);
            styleLike = styleLikeRepository.save(
                    originalStyleLike.get().updateStatus(Status.ACTIVATED)
            );
        } else {
            styleLike = styleLikeRepository.save(
                    StyleLike.builder()
                            .user(user)
                            .styleIdx(styleIdx)
                            .status(Status.ACTIVATED)
                            .build()
            );

        }

        return new PostStyleLikeRes(
                styleLike.getIdx(),
                styleLike.getUser().getIdx(),
                styleLike.getStyleIdx(),
                styleLike.getStatus()
        );

    }

    public PostStyleLikeRes deleteStyleLike(Long userIdx, Long styleIdx) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        styleRepository.findByIdxAndStatus(styleIdx, Status.ACTIVATED);
        StyleLike originalStyleLike = styleLikeRepository.findByUserIdxAndStyleIdx(userIdx, styleIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STYLE_LIKE_NO_MATCH));

        if (originalStyleLike.getStatus() != Status.ACTIVATED)
            throw new BaseException(BaseResponseStatus.STYLE_LIKE_NO_MATCH);

        StyleLike styleLike = styleLikeRepository.save(
                originalStyleLike.updateStatus(Status.DELETED));

        return new PostStyleLikeRes(
                styleLike.getIdx(),
                styleLike.getUser().getIdx(),
                styleLike.getStyleIdx(),
                styleLike.getStatus()
        );
    }

    public Long createStyle(
            Long userIdx,
            List<MultipartFile> images,
            String content,
           List<Long> products
    ) throws BaseException {
        User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        Style style = styleRepository.save(
                Style.builder()
                        .user(user)
                        .content(content)
                        .viewCount(0l)
                        .status(Status.ACTIVATED)
                        .build()
        );

        byte index = 0 ;
        if(images!=null) {
            for (MultipartFile image : images) {
                String imageUrl = s3Service.upload(image, "/static/styles");
                styleImageRepository.save(
                        StyleImage
                                .builder()
                                .style(style)
                                .image(imageUrl)
                                .position(index++)
                                .status(Status.ACTIVATED)
                                .build());
            }
        }

        List<StyleProduct> uploadedProducts = new ArrayList<>();
        for (Long product : products) {
            Product productEntity = productProvider.getProductByIdxAndStatus(product, Status.ACTIVATED);
            uploadedProducts.add(
                    styleProductRepository.save(
                            StyleProduct.builder()
                                    .product(productEntity)
                                    .style(style)
                                    .status(Status.ACTIVATED)
                                    .build()
                    ));
        }

        return style.getIdx();
    }
}
