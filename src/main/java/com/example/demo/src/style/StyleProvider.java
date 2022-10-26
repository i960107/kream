package com.example.demo.src.style;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.bid.BidProvider;
import com.example.demo.src.style.model.dto.*;
import com.example.demo.src.style.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StyleProvider {
    private final StyleRepository styleRepository;
    private final StyleLikeRepository styleLikeRepository;
    private final StyleCommentRepository styleCommentRepository;
    private final StyleProductRepository styleProductRepository;
    private final BidProvider bidProvider;
    private final StyleHashTagRepository styleHashTagRepository;
    private final HashTagRepository hashTagRepository;
    private final StyleImageRepository styleImageRepository;

    public GetStyleListRes retrieveStyleList(
            Pageable pageable
    ) throws BaseException {

        List<GetStyleRes> styles = new ArrayList<>();
        Page<Style> stylePage = styleRepository.findAllByStatus(Status.ACTIVATED, pageable);
        stylePage
                .stream()
                .forEach(entity -> {
                    Long liked = styleLikeRepository.countByStyleIdxAndStatus(entity.getIdx(), Status.ACTIVATED);
                    Long commented = styleCommentRepository.countByStyleIdxAndStatus(entity.getIdx(), Status.ACTIVATED);
                    List<GetStyleImageRes> images = entity.getStyleImages().stream()
                            .map(styleImage -> new GetStyleImageRes(styleImage.getImage(), styleImage.getPosition()))
                            .collect(Collectors.toList());

                    List<GetStyleProductRes> products = styleProductRepository.findByStyleIdxAndStatus(entity.getIdx(), Status.ACTIVATED)
                            .stream()
                            .map(
                                    styleProduct -> {
                                        Long buyPrice = null;
                                        try {
                                            buyPrice = bidProvider.getMinSaleBidPrice(styleProduct.getProduct().getIdx());
                                        } catch (BaseException e) {

                                        }
                                        return new GetStyleProductRes(
                                                styleProduct.getProduct().getIdx(),
                                                styleProduct.getProduct().getThumbnail(),
                                                styleProduct.getProduct().getName(),
                                                buyPrice
                                        );
                                    }

                            ).collect(Collectors.toList());


                    styles.add(new GetStyleRes(
                            entity.getIdx(),
                            entity.getUser().getProfileImage(),
                            entity.getUser().getNickName(),
                            images,
                            entity.getContent(),
                            products,
                            liked,
                            commented,
                            entity.getCreatedAt()
                    ));
                });

        return new GetStyleListRes(
                styles,
                stylePage.getTotalPages(),
                stylePage.getTotalElements(),
                ((PageImpl) stylePage).getPageable().getPageNumber(),
                stylePage.isFirst(),
                stylePage.isLast()
        );
    }

    public List<GetStyleCommentRes> retrieveStyleComments(
            Long styleIdx
    ) throws BaseException {
        styleRepository.findByIdxAndStatus(styleIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STYLE_WRONG_IDX));

        List<StyleComment> children = styleCommentRepository
                .findByStyleIdxAndParentIdxIsNotNullAndStatus(styleIdx, Status.ACTIVATED, Sort.by(Sort.Direction.ASC, "createdAt"));
        List<StyleComment> parents = styleCommentRepository
                .findByStyleIdxAndParentIdxIsNullAndStatus(styleIdx, Status.ACTIVATED, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<GetStyleCommentRes> comments = new ArrayList<>();
        parents.stream().forEach(parentComment -> {
            List<GetStyleChildCommentRes> childComments = new ArrayList<>();
            buildTree(children, parentComment.getIdx(), childComments);
            comments.add(
                    new GetStyleCommentRes(
                            parentComment.getIdx(),
                            parentComment.getUser().getIdx(),
                            parentComment.getUser().getNickName(),
                            parentComment.getUser().getProfileImage(),
                            parentComment.getContent(),
                            parentComment.getCreatedAt(),
                            childComments
                    )
            );
        });
        return comments;
    }

    public void buildTree(
            List<StyleComment> comments,
            Long parentIdx,
            List<GetStyleChildCommentRes> childComments
    ) {
        comments.stream().forEach(comment -> {
            if (comment.getParentIdx() == parentIdx) {
                String commentedTo = styleCommentRepository.findNickNameByIdx(comment.getParentIdx());

                childComments.add(new GetStyleChildCommentRes(
                        comment.getIdx(),
                        comment.getUser().getIdx(),
                        comment.getUser().getNickName(),
                        comment.getUser().getProfileImage(),
                        commentedTo,
                        comment.getContent(),
                        comment.getCreatedAt()
                ));
                buildTree(comments, comment.getIdx(), childComments);
            }
        });
    }

    public List<GetStyleLikeRes> retrieveStyleLikes(Long styleIdx) throws BaseException {
        styleRepository.findByIdxAndStatus(styleIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STYLE_WRONG_IDX));
        List<GetStyleLikeRes> styleLikes = styleLikeRepository.findByStyleIdxAndStatus(styleIdx, Status.ACTIVATED)
                .stream()
                .map(entity -> new GetStyleLikeRes(
                        entity.getIdx(),
                        entity.getUser().getIdx(),
                        entity.getUser().getNickName(),
                        entity.getUser().getProfileImage(),
                        entity.getUser().getName()))
                .collect(Collectors.toList());
        return styleLikes;
    }

    public GetStyleListRes retrieveStyleByHashTag(String hashTag, Pageable pageable) {
        Long hashtagIdx = hashTagRepository.findByNameAndStatus(hashTag, Status.ACTIVATED);
        List<GetStyleRes> styles = new ArrayList<>();
        Page<StyleHashTag> styleHashTagPage = null;
        if (hashtagIdx != null) {
             styleHashTagPage = styleHashTagRepository
                    .findByHashtagIdxAndStatus(hashtagIdx, Status.ACTIVATED, pageable);
            styleHashTagPage
                    .stream()
                    .forEach(entity -> {
                        Style style = entity.getStyle();
                        Long liked = styleLikeRepository.countByStyleIdxAndStatus(style.getIdx(), Status.ACTIVATED);
                        Long commented = styleCommentRepository.countByStyleIdxAndStatus(style.getIdx(), Status.ACTIVATED);
                        List<GetStyleImageRes> images = style.getStyleImages().stream()
                                .map(styleImage -> new GetStyleImageRes(styleImage.getImage(), styleImage.getPosition()))
                                .collect(Collectors.toList());

                        List<GetStyleProductRes> products = styleProductRepository
                                .findByStyleIdxAndStatus(style.getIdx(), Status.ACTIVATED)
                                .stream()
                                .map(
                                        styleProduct -> {
                                            Long buyPrice = null;
                                            try {
                                                buyPrice = bidProvider.getMinSaleBidPrice(styleProduct.getProduct().getIdx());
                                            } catch (BaseException e) {

                                            }
                                            return new GetStyleProductRes(
                                                    styleProduct.getProduct().getIdx(),
                                                    styleProduct.getProduct().getThumbnail(),
                                                    styleProduct.getProduct().getName(),
                                                    buyPrice
                                            );
                                        }

                                ).collect(Collectors.toList());


                        styles.add(new GetStyleRes(
                                style.getIdx(),
                                style.getUser().getProfileImage(),
                                style.getUser().getNickName(),
                                images,
                                style.getContent(),
                                products,
                                liked,
                                commented,
                                style.getCreatedAt()
                        ));
                    });
        return new GetStyleListRes(
                styles,
                styleHashTagPage.getTotalPages(),
                styleHashTagPage.getTotalElements(),
                ((PageImpl) styleHashTagPage).getPageable().getPageNumber(),
                styleHashTagPage.isFirst(),
                styleHashTagPage.isLast()
        );
        }else return null;
    }

    public PostStyleRes retrieveStyle(Long uploadedStyleIdx) throws BaseException{
         Style style = styleRepository.findByIdxAndStatus(uploadedStyleIdx, Status.ACTIVATED)
                .orElseThrow(()->new BaseException(BaseResponseStatus.STYLE_WRONG_IDX));
         List<GetStyleImageRes> images = styleImageRepository.findByStyleIdxAndStatus(uploadedStyleIdx,Status.ACTIVATED)
                 .stream()
                 .map(entity -> new GetStyleImageRes(entity.getImage(), entity.getPosition()))
                 .collect(Collectors.toList());
         List<Long> products= styleProductRepository
                 .findByStyleIdxAndStatus(uploadedStyleIdx, Status.ACTIVATED)
                 .stream().map(entity->entity.getIdx()).collect(Collectors.toList());

         return new PostStyleRes(
                style.getIdx(),
                style.getUser().getIdx(),
                images,
                style.getContent(),
                products,
                style.getCreatedAt()
         );
    }
}