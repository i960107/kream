package com.example.demo.src.bid;

import com.example.demo.config.Auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Role;
import com.example.demo.src.bid.sale.model.dto.GetBidSaleListRes;
import com.example.demo.src.bid.sale.model.dto.PostBidSaleNowReq;
import com.example.demo.src.bid.sale.model.dto.PostBidSaleNowRes;
import com.example.demo.src.bid.purchase.model.dto.*;
import com.example.demo.src.product.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidController {
    private final BidProvider bidProvider;
    private final BidService bidService;


    @GetMapping("/api/bids/sale")
    private BaseResponse<List<GetProductPurchasePriceRes>> retrieveProductPurchasePrice(
            @RequestParam("productIdx") Long productIdx
    ) throws BaseException {
        List<GetProductPurchasePriceRes> responseDto = bidProvider.retrieveProductPurchasePrice(productIdx);
        return new BaseResponse<List<GetProductPurchasePriceRes>>(responseDto);
    }

    @GetMapping("/api/bids/purchase")
    private BaseResponse<List<GetProductSalePriceRes>> retrieveProductSalePrice(
            @RequestParam("productIdx") Long productIdx
    ) throws BaseException {
        List<GetProductSalePriceRes> responseDto = bidProvider.retrieveProductSalePrice(productIdx);
        return new BaseResponse<List<GetProductSalePriceRes>>(responseDto);
    }

    @GetMapping("/api/products/{productIdx}/purchases")
    public BaseResponse<GetBidPurchaseListRes> retrieveBidPurchaseList(
            @PathVariable("productIdx") Long productIdx,
            @RequestParam(value = "productSizeIdx", required = false) Long productSizeIdx,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0, sort = ("createdAt"), direction = Sort.Direction.DESC) Pageable pageable
    ) throws BaseException {
        GetBidPurchaseListRes responseDto = bidProvider.retrieveBidPurchaseList(productIdx, productSizeIdx, pageable);
        return new BaseResponse<GetBidPurchaseListRes>(responseDto);
    }

    @GetMapping("/api/products/{productIdx}/sales")
    public BaseResponse<GetBidSaleListRes> retrieveBidSaleList(
            @PathVariable("productIdx") Long productIdx,
            @RequestParam(value = "productSizeIdx", required = false) Long productSizeIdx,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0, sort = ("createdAt"), direction = Sort.Direction.DESC) Pageable pageable
    ) throws BaseException {
        GetBidSaleListRes responseDto = bidProvider.retrieveBidSaleList(productIdx, productSizeIdx, pageable);
        return new BaseResponse<GetBidSaleListRes>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/bids/purchases/{bidPurchaseIdx}/purchase-now")
    private BaseResponse<PostBidPurchaseNowRes> createBidPurchaseNow(
            @Valid @RequestBody PostBidPurchaseNowReq postBidPurchaseNowReq,
            @RequestAttribute("userIdx") Long userIdx,
            @PathVariable("bidPurchaseIdx") Long bidPurchaseIdx
    ) throws BaseException{
        PostBidPurchaseNowRes responseDto = bidService.createBidPurchaseNow( userIdx, bidPurchaseIdx,postBidPurchaseNowReq);
        return new BaseResponse<>(responseDto);

    }

    @Auth(role = Role.USER)
    @PostMapping("/api/products/{productIdx}/purchase")
    private BaseResponse<PostProductPurchaseRes> createProductPurchase(
            @Valid @RequestBody PostProductPurchaseReq postProductPurchaseReq,
            @RequestAttribute("userIdx") Long userIdx,
            @PathVariable("productIdx") Long productIdx
    ) throws BaseException {
        PostProductPurchaseRes responseDto = bidService.createProductPurchase(userIdx, productIdx,postProductPurchaseReq);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/bids/sales/{bidSaleIdx}/sale-now")
    private BaseResponse<PostBidSaleNowRes> createBidSaleNow(
            @Valid @RequestBody PostBidSaleNowReq postBidSaleNowReq,
            @RequestAttribute("userIdx") Long userIdx,
            @PathVariable("bidSaleIdx") Long bidSaleIdx
    ) throws BaseException {
        PostBidSaleNowRes responseDto = bidService.createBidSaleNow(postBidSaleNowReq, userIdx, bidSaleIdx);
        return new BaseResponse<>(responseDto);
    }

    @Auth(role = Role.USER)
    @PostMapping("/api/products/{productIdx}/sale")
    private BaseResponse<PostProductSaleRes> createBidSale(
            @Valid @RequestBody PostProductSaleReq postProductSaleReq,
            @RequestAttribute("userIdx") Long userIdx,
            @PathVariable("productIdx") Long productIdx
    ) throws BaseException {
        PostProductSaleRes responseDto = bidService.createBidSale(postProductSaleReq, userIdx, productIdx);
        return new BaseResponse<>(responseDto);
    }
}
