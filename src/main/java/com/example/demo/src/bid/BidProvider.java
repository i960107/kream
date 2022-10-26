package com.example.demo.src.bid;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.bid.purchase.model.dto.GetBidPurchaseListRes;
import com.example.demo.src.bid.purchase.model.dto.GetBidPurchaseRes;
import com.example.demo.src.bid.purchase.model.dto.GetUserBidPurchaseListRes;
import com.example.demo.src.bid.purchase.model.dto.GetUserBidPurchaseListResBid;
import com.example.demo.src.bid.purchase.model.entity.BidPurchase;
import com.example.demo.src.bid.purchase.model.entity.BidPurchaseRepository;
import com.example.demo.src.bid.sale.model.dto.*;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import com.example.demo.src.bid.sale.model.entity.BidSaleRepository;
import com.example.demo.src.product.model.dto.GetProductPurchasePriceRes;
import com.example.demo.src.product.model.dto.GetProductSalePriceRes;
import com.example.demo.src.product.model.entity.Product;
import com.example.demo.src.product.model.entity.ProductRepository;
import com.example.demo.src.product.model.entity.ProductSize;
import com.example.demo.src.product.model.entity.ProductSizeRepository;
import com.example.demo.src.user.model.entity.User;
import com.example.demo.src.user.model.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidProvider {
    private final BidPurchaseRepository bidPurchaseRepository;
    private final BidSaleRepository bidSaleRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    public List<GetProductPurchasePriceRes> retrieveProductPurchasePrice(
            Long productIdx
    ) throws BaseException {
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED, Sort.by(Sort.Direction.ASC, "productSizes.size"))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        List<ProductSize> productSizeList = product.getProductSizes();

        List<GetProductPurchasePriceRes> productPurchasePriceList = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

        Long minBidAllSize = bidSaleRepository.findMinBidPrice(
                productSizeList
                        .stream()
                        .map(entity -> entity.getIdx())
                        .collect(Collectors.toList()),
                Status.BIDDING
        );

        productPurchasePriceList.add(
                new GetProductPurchasePriceRes(
                        null,
                        "모든 사이즈",
                        minBidAllSize,
                        null
                )
        );

        for (ProductSize productSize : productSizeList) {
            Long minBidSalePrice = bidSaleRepository.findMinBidPrice(productSize.getIdx(), Status.BIDDING);
            if (minBidSalePrice != null) {
                BidSale bidSale = bidSaleRepository.findFirstByBidPriceAndProductSizeIdxAndStatus(
                        minBidSalePrice,
                        productSize.getIdx(),
                        Status.BIDDING,
                        sort);
                productPurchasePriceList.add(
                        new GetProductPurchasePriceRes(
                                productSize.getIdx(),
                                bidSale.getProductSize().getSize(),
                                bidSale.getBidPrice(),
                                bidSale.getIdx()));
            } else {
                productPurchasePriceList.add(
                        new GetProductPurchasePriceRes(
                                productSize.getIdx(),
                                productSize.getSize(),
                                null,
                                null
                        )
                );
            }
        }
        return productPurchasePriceList;
    }

    public List<GetProductSalePriceRes> retrieveProductSalePrice(
            Long productIdx
    ) throws BaseException {
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED, Sort.by(Sort.Direction.ASC, "productSizes.size"))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        List<ProductSize> productSizeList = product.getProductSizes();

        List<GetProductSalePriceRes> productSalePriceRes = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

        Long maxBidAllSize = bidPurchaseRepository
                .findMaxBidPrice(productSizeList
                        .stream()
                        .map(entity -> entity.getIdx())
                        .collect(Collectors.toList()), Status.BIDDING);

        productSalePriceRes.add(new GetProductSalePriceRes(null, "모든 사이즈", maxBidAllSize, null));

        for (ProductSize productSize : productSizeList) {
            Long maxBidPurchasePrice = bidPurchaseRepository.findMaxBidPrice(productSize.getIdx(), Status.BIDDING);
            if (maxBidPurchasePrice != null) {
                BidPurchase bidPurchase = bidPurchaseRepository.findFirstByBidPriceAndProductSizeIdxAndStatus(
                        maxBidPurchasePrice,
                        productSize.getIdx(),
                        Status.BIDDING,
                        sort);
                productSalePriceRes.add(
                        new GetProductSalePriceRes(
                                productSize.getIdx(),
                                bidPurchase.getProductSize().getSize(),
                                bidPurchase.getBidPrice(),
                                bidPurchase.getIdx()));
            } else {
                productSalePriceRes.add(
                        new GetProductSalePriceRes(
                                productSize.getIdx(),
                                productSize.getSize(),
                                null,
                                null
                        )
                );
            }
        }
        return productSalePriceRes;

    }
    public GetBidSaleListRes retrieveBidSaleList(
            Long productIdx,
            Long productSizeIdx,
            Pageable pageable
    ) throws BaseException {
        Product product = productRepository
                .findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        Page<GetBidSaleRes> bidSaleList;

        if (productSizeIdx == null) {
            List<Long> productSizeList = product.getProductSizes()
                    .stream()
                    .map(entity -> entity.getIdx()).collect(Collectors.toList());
            bidSaleList = bidSaleRepository.findByProductSizeIdxInAndStatus(productSizeList, Status.BIDDING, pageable);
        } else {
            if (!productSizeRepository.existsByProductIdxAndIdxAndStatus(productIdx, productSizeIdx, Status.ACTIVATED))
                throw new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX);
            bidSaleList = bidSaleRepository.findByProductSizeIdxAndStatus(productSizeIdx, Status.BIDDING, pageable);
        }
        return new GetBidSaleListRes(bidSaleList);
    }

    public GetBidPurchaseListRes retrieveBidPurchaseList(
            Long productIdx,
            Long productSizeIdx,
            Pageable pageable
    ) throws BaseException {
        Product product = productRepository
                .findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        Page<GetBidPurchaseRes> bidPurchaseList;

        if (productSizeIdx == null) {
            List<Long> productSizeList = product.getProductSizes()
                    .stream()
                    .map(entity -> entity.getIdx()).collect(Collectors.toList());
            bidPurchaseList = bidPurchaseRepository.findByProductSizeIdxInAndStatus(productSizeList, Status.BIDDING, pageable);
        } else {
            if (!productSizeRepository.existsByProductIdxAndIdxAndStatus(productIdx, productSizeIdx, Status.ACTIVATED))
                throw new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX);
            bidPurchaseList = bidPurchaseRepository.findByProductSizeIdxAndStatus(productSizeIdx, Status.BIDDING, pageable);
        }
        return new GetBidPurchaseListRes(bidPurchaseList);
    }

    public Boolean isBidSaleExist(Long bidSaleIdx, Status status) throws BaseException{
        return (bidSaleRepository
                .findByIdxAndStatus(bidSaleIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.BID_NOT_PROCEEDING))!=null);
    }
    public Boolean isBidPurchaseExist(Long bidPurchaseIdx, Status status) throws BaseException{
        return (bidPurchaseRepository
                .findByIdxAndStatus(bidPurchaseIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.BID_NOT_PROCEEDING))!=null);
    }
    public Boolean isPaymentInfoValid(Long bidPurchaseIdx, Long cardIdx,Long totalPrice,Status status) throws BaseException{
        return (bidPurchaseRepository
                .findByIdxAndCardIdxAndTotalPriceAndStatus(bidPurchaseIdx,cardIdx,totalPrice,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.BID_NOT_MATCH_PAYMENT))!=null);
    }
    public Long getMinSaleBidPrice(Long productIdx) throws BaseException{

        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED, Sort.by(Sort.Direction.ASC, "productSizes.size"))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        List<ProductSize> productSizeList = product.getProductSizes();

        Long minBidAllSize = bidSaleRepository.findMinBidPrice(
                productSizeList
                        .stream()
                        .map(entity -> entity.getIdx())
                        .collect(Collectors.toList()),
                Status.BIDDING
        );
        return minBidAllSize;
    }
}
