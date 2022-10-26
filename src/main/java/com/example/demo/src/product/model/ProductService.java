package com.example.demo.src.product.model;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.bid.purchase.model.entity.BidPurchase;
import com.example.demo.src.bid.purchase.model.entity.BidPurchaseRepository;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import com.example.demo.src.bid.sale.model.entity.BidSaleRepository;
import com.example.demo.src.product.model.dto.*;
import com.example.demo.src.product.model.entity.*;
import com.example.demo.src.transaction.model.TransactionProvider;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductLikeRepository productLikeRepository;
    private final BidSaleRepository bidSaleRepository;
    private final AddressRepository addressRepository;
    private final BidPurchaseRepository bidPurchaseRepository;
    private final UserProvider userProvider;

    public PostUserLikeRes createProductLike(Long productIdx, Long userIdx, Long productSizeIdx) throws BaseException {

       User user = userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));
        ProductSize productSize = productSizeRepository.findByIdxAndProductIdxAndStatus(productSizeIdx, productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX));

        Optional<ProductLike> originalProductLike = productLikeRepository.findByProductSizeIdxAndUserIdx(productSizeIdx, userIdx);
        ProductLike productLike;
        if (originalProductLike.isPresent()) {
            if (originalProductLike.get().getStatus() == Status.ACTIVATED)
                throw new BaseException(BaseResponseStatus.PRODUCT_LIKE_ALREADY_EXIST);
            productLike = productLikeRepository.save(originalProductLike.get().activateProductLike());
        } else {
            productLike = productLikeRepository.save(
                    ProductLike.builder().
                            user(user).
                            productSize(productSize).
                            status(Status.ACTIVATED).
                            build());
        }
        return new PostUserLikeRes(
                productLike.getIdx(),
                productLike.getUser().getIdx(),
                productLike.getProductSize().getIdx()
        );
    }

    public DeleteProductLikeRes deleteProductLike(Long productIdx, Long productSizeIdx, Long userIdx) throws BaseException {
        userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        ProductLike originalProductLike = productLikeRepository.findByProductSizeIdxAndUserIdxAndStatus(productSizeIdx, userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_LIKE_NO_MATCH));

        ProductLike changedProductLike = originalProductLike.deleteProductLike();

        ProductLike productLike = productLikeRepository.save(changedProductLike);

        return new DeleteProductLikeRes(
                productLike.getIdx(),
                productLike.getUser().getIdx(),
                productLike.getProductSize().getIdx(),
                productLike.getStatus());
    }


    public PostProductPurchaseNowRes createProductPurchaseNow(Long userIdx, Long productIdx, PostProductPurchaseNowReq postProductPurchaseNowReq) throws BaseException {
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        ProductSize productSize = productSizeRepository.findByIdxAndProductIdxAndStatus(postProductPurchaseNowReq.getProductSizeIdx(), productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX));

        BidSale bidSale = bidSaleRepository.findByIdxAndStatus(postProductPurchaseNowReq.getTargetBidSaleIdx(), Status.BIDDING)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TARGET_BID_WRONG_IDX));

        if (!bidSale.getBidPrice() .equals(postProductPurchaseNowReq.getPurchasePrice()))
            throw new BaseException(BaseResponseStatus.PRICE_NOT_MATCH_BID_PRICE);

        if (!postProductPurchaseNowReq.getTotalPrice()
                .equals( postProductPurchaseNowReq.getPurchasePrice()
                + postProductPurchaseNowReq.getInspectionFee()
                + postProductPurchaseNowReq.getShippingFee()))
            throw new BaseException(BaseResponseStatus.WRONG_TOTAL_PRICE);

        Address address = addressRepository.findByIdxAndUserIdxAndStatus(postProductPurchaseNowReq.getAddressIdx(),userIdx,Status.ACTIVATED)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_ADDRESS_IDX));

        BidPurchase bidPurchase = bidPurchaseRepository.save(
                BidPurchase.builder()
                        .userIdx(userIdx)
                        .productSize(productSize)
                        .bidPrice(postProductPurchaseNowReq.getPurchasePrice())
                        .deadline(LocalDate.now())
                        .address(address)
                        .point(postProductPurchaseNowReq.getPoint())
                        .inspectionFee(postProductPurchaseNowReq.getInspectionFee())
                        .shippingFee(postProductPurchaseNowReq.getShippingFee())
                        .totalPrice(postProductPurchaseNowReq.getTotalPrice())
                        .status(Status.REGISTERING)
                        .build()
        );

        return new PostProductPurchaseNowRes(
                bidPurchase.getIdx(),
                bidSale.getIdx(),
                bidPurchase.getProductSize().getProduct().getThumbnail(),
                bidPurchase.getProductSize().getProduct().getModelNo(),
                bidPurchase.getProductSize().getProduct().getName(),
                bidPurchase.getProductSize().getSize(),
                bidPurchase.getAddress().getName(),
                bidPurchase.getAddress().getPhone(),
                bidPurchase.getAddress().getAddress(),
                bidPurchase.getAddress().getAddressDetail(),
                bidPurchase.getAddress().getZipCode(),
                bidPurchase.getTotalPrice(),
                bidPurchase.getBidPrice(),
                bidPurchase.getInspectionFee(),
                bidPurchase.getShippingFee(),
                bidPurchase.getStatus()
        );
    }

    public PostProductSaleNowRes createProductSaleNow(
            Long userIdx,
            Long productIdx,
            PostProductSaleNowReq postProductSaleNowReq
    ) throws BaseException{
        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        ProductSize productSize = productSizeRepository.findByIdxAndProductIdxAndStatus(postProductSaleNowReq.getProductSizeIdx(), productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX));

        BidPurchase bidPurchase = bidPurchaseRepository.findByIdxAndStatus(postProductSaleNowReq.getTargetBidPurchaseIdx(), Status.BIDDING)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TARGET_BID_WRONG_IDX));

        Account account = userProvider.getAccountByIdxAndUserIdxAndStatus(postProductSaleNowReq.getAccountIdx(), userIdx,Status.ACTIVATED);

        if (!bidPurchase.getBidPrice().equals(postProductSaleNowReq.getSalePrice()))
            throw new BaseException(BaseResponseStatus.PRICE_NOT_MATCH_BID_PRICE);

        if (!postProductSaleNowReq.getSettlementAmount()
                .equals( postProductSaleNowReq.getSalePrice()
                        -postProductSaleNowReq.getCommission()
                        - postProductSaleNowReq.getInspectionFee()
                        - postProductSaleNowReq.getShippingFee()))
            throw new BaseException(BaseResponseStatus.WRONG_TOTAL_PRICE);

        Address address = addressRepository.findByIdxAndUserIdxAndStatus(postProductSaleNowReq.getAddressIdx(),userIdx,Status.ACTIVATED)
                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_ADDRESS_IDX));


        BidSale bidSale = bidSaleRepository.save(
                BidSale.builder()
                        .userIdx(userIdx)
                        .productSize(productSize)
                        .bidPrice(postProductSaleNowReq.getSalePrice())
                        .deadline(LocalDate.now())
                        .address(address)
                        .commission(postProductSaleNowReq.getCommission())
                        .inspectionFee(postProductSaleNowReq.getInspectionFee())
                        .shippingFee(postProductSaleNowReq.getShippingFee())
                        .account(account)
                        .settlementAmount(postProductSaleNowReq.getSettlementAmount())
                        .status(Status.REGISTERING)
                        .build()
        );
        return new PostProductSaleNowRes(
                bidSale.getIdx(),
                bidPurchase.getIdx(),
                product.getThumbnail(),
                product.getModelNo(),
                product.getName(),
                productSize.getSize(),
                account.getBank(),
                account.getAccount(),
                account.getAccountHolder(),
                bidSale.getAddress().getName(),
                bidSale.getAddress().getAddress(),
                bidSale.getAddress().getAddressDetail(),
                bidSale.getAddress().getZipCode(),
                bidSale.getAddress().getPhone(),
                bidSale.getSettlementAmount(),
                bidSale.getBidPrice(),
                bidSale.getInspectionFee(),
                bidSale.getShippingFee(),
                bidSale.getCommission(),
                bidSale.getStatus()
        );
    }
    }
//    public PostProductPurchaseRes createProductPurchase(Long userIdx, Long productIdx, PostProductPurchaseReq postProductPurchaseReq) throws BaseException{
//        Product product = productRepository.findByIdxAndStatus(productIdx, Status.ACTIVATED)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));
//
//        ProductSize productSize = productSizeRepository.findByIdxAndProductIdxAndStatus(postProductPurchaseReq.getProductSizeIdx(), productIdx, Status.ACTIVATED)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX));
//
//        Long minBidPrice =bidSaleRepository.findMinBidPrice(productSize.getIdx(),Status.BIDDING);
//
//        if(postProductPurchaseReq.getPurchasePrice()>=minBidPrice) throw new BaseException(BaseResponseStatus.BID_PURCHASE_GREATER_THAN_MIN_SALE);
//
//        if (!postProductPurchaseReq.getTotalPrice()
//                .equals( postProductPurchaseReq.getPurchasePrice()
//                        + postProductPurchaseReq.getInspectionFee()
//                        + postProductPurchaseReq.getShippingFee()))
//            throw new BaseException(BaseResponseStatus.WRONG_TOTAL_PRICE);
//
//        Address address = addressRepository.findByIdxAndUserIdxAndStatus(postProductPurchaseReq.getAddressIdx(),userIdx,Status.ACTIVATED)
//                .orElseThrow(()->new BaseException(BaseResponseStatus.USERS_WRONG_ADDRESS_IDX));
//
//        BidPurchase bidPurchase = bidPurchaseRepository.save(
//                BidPurchase.builder()
//                        .userIdx(userIdx)
//                        .productSize(productSize)
//                        .bidPrice(postProductPurchaseReq.getPurchasePrice())
//                        .deadline(postProductPurchaseReq.getDeadline())
//                        .address(address)
//                        .point(postProductPurchaseReq.getPoint())
//                        .inspectionFee(postProductPurchaseReq.getInspectionFee())
//                        .shippingFee(postProductPurchaseReq.getShippingFee())
//                        .totalPrice(postProductPurchaseReq.getTotalPrice())
//                        .status(Status.REGISTERING)
//                        .build()
//        );
//        return new PostProductPurchaseRes(
//                bidPurchase.getIdx(),
//                bidPurchase.getProductSize().getProduct().getThumbnail(),
//                bidPurchase.getProductSize().getProduct().getModelNo(),
//                bidPurchase.getProductSize().getProduct().getName(),
//                bidPurchase.getProductSize().getSize(),
//                bidPurchase.getAddress().getName(),
//                bidPurchase.getAddress().getAddressDetail(),
//                bidPurchase.getAddress().getAddressDetail(),
//                bidPurchase.getAddress().getZipCode(),
//                bidPurchase.getTotalPrice(),
//                bidPurchase.getBidPrice(),
//                bidPurchase.getInspectionFee(),
//                bidPurchase.getShippingFee(),
//                bidPurchase.getCreatedAt().toLocalDate(),
//                bidPurchase.getDeadline(),
//                bidPurchase.getStatus()
//        );
//    }
//}
