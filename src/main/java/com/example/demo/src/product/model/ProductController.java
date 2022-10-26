package com.example.demo.src.product.model;

import com.example.demo.config.Auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Role;
import com.example.demo.src.bid.BidProvider;
import com.example.demo.src.bid.purchase.model.dto.GetBidPurchaseListRes;
import com.example.demo.src.bid.sale.model.dto.GetBidSaleListRes;
import com.example.demo.src.product.model.dto.*;
import com.example.demo.src.transaction.model.TransactionProvider;
import com.example.demo.src.transaction.model.dto.GetTransactionListRes;
import com.example.demo.utils.ValidationRegex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductProvider productProvider;
    private final TransactionProvider transactionProvider;
    private final BidProvider bidProvider;

    @GetMapping("/api/products/category")
    private BaseResponse<GetProductCategoryDetailListRes> retrieveProductCategoryList(@Valid @RequestParam("exposure") Boolean exposure) throws BaseException {
        GetProductCategoryDetailListRes responseDto = productProvider.retrieveProductCategoryList(exposure);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/api/products/brands")
    private BaseResponse<GetBrandListRes> retrieveBrandList() throws BaseException {
        GetBrandListRes responseDto = productProvider.retrieveBrandList();
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/api/products")
    private BaseResponse<GetProductListRes> retrieveProductList(
            @RequestParam(value = "categoryIdx", required = false) Long categoryIdx,
            @RequestParam(value = "categoryDetailIdx", required = false) Long categoryDetailIdx,
            @Valid @Pattern(regexp = ValidationRegex.regexBoolean) @RequestParam(value = "luxury", required = false) String luxury,
            @RequestParam(value = "brand", required = false) List<String> brands,
            @RequestParam(value = "sneakersSize", required = false) Byte sneakersSize,
            @RequestParam(value = "minPrice", required = false) Long minPrice,
            @RequestParam(value = "maxPrice", required = false) Long maxPrice,
            @RequestParam(value = "keyword", required = false) String keyword
    ) throws BaseException {
        GetProductListRes responseDto = productProvider.retrieveProductList(categoryIdx,
                categoryDetailIdx,
                luxury,
                brands,
                sneakersSize,
                minPrice,
                maxPrice,
                keyword
                );
        return new BaseResponse<GetProductListRes>(responseDto);
    }

    @GetMapping("/api/products/{productIdx}")
    private BaseResponse<GetProductRes> retrieveProduct(
            @PathVariable("productIdx") Long productIdx
    ) throws BaseException {
        GetProductRes responseDto = productProvider.retrieveProduct(productIdx);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/api/products/{productIdx}/transactions")
    public BaseResponse<GetTransactionListRes> retrieveTransactionList(
            @PathVariable(name = "productIdx") Long productIdx,
            @RequestParam(name = "productSizeIdx", required = false) Long productSizeIdx,
            @RequestParam(name = "duration", required = false) Integer duration,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) throws BaseException {
        GetTransactionListRes responseDto = transactionProvider.retrieveTransactionList(productIdx, productSizeIdx, duration, pageable);
        return new BaseResponse<GetTransactionListRes>(responseDto);
    }

    @GetMapping("/api/products/{productIdx}/sizes")
    private BaseResponse<GetProductSizeListRes> retrieveProductSizeList(
            @PathVariable("productIdx") Long productIdx
    ) throws BaseException {
        GetProductSizeListRes responseDto = productProvider.retrieveProductSizeList(productIdx);
        return new BaseResponse<GetProductSizeListRes>(responseDto);
    }

    @GetMapping("/api/products/{productIdx}/others")
    private BaseResponse<GetProductOthersListRes> retrieveProductOthersList(
            @PathVariable("productIdx") Long productIdx,
            @PageableDefault(size = 5) Pageable pageable
    ) throws BaseException {
        GetProductOthersListRes responseDto = productProvider.retrieveProductOthersList(productIdx, pageable);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/products/{productIdx}/like")
    private BaseResponse<PostUserLikeRes> createProductLike(
            @PathVariable("productIdx") Long productIdx,
            @Valid @RequestBody PostUserLikeReq requestDto,
            @RequestAttribute("userIdx") Long userIdx
    ) throws BaseException {
        PostUserLikeRes responseDto = productService.createProductLike(productIdx, userIdx, requestDto.getProductSizeIdx());
        return new BaseResponse<PostUserLikeRes>(responseDto);
    }

    @Auth(role = Role.USER)
    @DeleteMapping("/api/products/{productIdx}/like")
    private BaseResponse<DeleteProductLikeRes> deleteProductLike(
            @PathVariable("productIdx") Long productIdx,
            @Valid @RequestBody PostUserLikeReq requestDto,
            @RequestAttribute("userIdx") Long userIdx
    ) throws BaseException {
        DeleteProductLikeRes responseDto = productService.deleteProductLike(productIdx, requestDto.getProductSizeIdx(), userIdx);
        return new BaseResponse<DeleteProductLikeRes>(responseDto);
    }


    @GetMapping("/api/products/recommend")
    private BaseResponse<List<GetProductRecommendRes>> retrieveProductRecommend() throws BaseException {
        List<GetProductRecommendRes> responseDto = productProvider.retrieveProductRecommend();
        return new BaseResponse<List<GetProductRecommendRes>>(responseDto);
    }

    @GetMapping("/api/products/ads")
    private BaseResponse<List<GetProductAdsRes>> retrieveProductAds() throws BaseException {
        List<GetProductAdsRes> responseDto = productProvider.retrieveProductAds();
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/products/{productIdx}/purchase-now")
    private BaseResponse<PostProductPurchaseNowRes> createProductPurchaseNow(
            @Valid @RequestBody PostProductPurchaseNowReq postProductPurchaseNowReq,
            @RequestAttribute("userIdx") Long userIdx,
            @PathVariable("productIdx") Long productIdx
    ) throws BaseException {
        PostProductPurchaseNowRes responseDto = productService.createProductPurchaseNow(userIdx, productIdx,postProductPurchaseNowReq);
        return new BaseResponse<>(responseDto);
    }


    @Auth(role = Role.USER)
    @PostMapping("/api/products/{productIdx}/sale-now")
    private BaseResponse<PostProductSaleNowRes> createProductSaleNow(
            @Valid @RequestBody PostProductSaleNowReq postProductSaleNowReq,
            @RequestAttribute("userIdx") Long userIdx,
            @PathVariable("productIdx") Long productIdx
    ) throws BaseException {
        PostProductSaleNowRes responseDto = productService.createProductSaleNow(userIdx, productIdx,postProductSaleNowReq);
        return new BaseResponse<>(responseDto);
    }
}
