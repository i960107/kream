package com.example.demo.src.bid;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.bid.sale.model.dto.PostBidSaleNowReq;
import com.example.demo.src.bid.sale.model.dto.PostBidSaleNowRes;
import com.example.demo.src.bid.purchase.model.dto.PostBidPurchaseNowReq;
import com.example.demo.src.bid.purchase.model.dto.PostBidPurchaseNowRes;
import com.example.demo.src.bid.purchase.model.entity.BidPurchase;
import com.example.demo.src.bid.purchase.model.entity.BidPurchaseRepository;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import com.example.demo.src.bid.sale.model.entity.BidSaleRepository;
import com.example.demo.src.product.model.ProductProvider;
import com.example.demo.src.product.model.dto.PostProductPurchaseReq;
import com.example.demo.src.product.model.dto.PostProductPurchaseRes;
import com.example.demo.src.product.model.dto.PostProductSaleReq;
import com.example.demo.src.product.model.dto.PostProductSaleRes;
import com.example.demo.src.product.model.entity.Product;
import com.example.demo.src.product.model.entity.ProductRepository;
import com.example.demo.src.product.model.entity.ProductSize;
import com.example.demo.src.product.model.entity.ProductSizeRepository;
import com.example.demo.src.transaction.model.TransactionService;
import com.example.demo.src.transaction.model.entity.Payment;
import com.example.demo.src.transaction.model.entity.Transaction;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false, rollbackFor = {BaseException.class, RuntimeException.class})
public class BidService {
    private final BidSaleRepository bidSaleRepository;
    private final BidPurchaseRepository bidPurchaseRepository;
    private final UserRepository userRepository;
    private final UserProvider userProvider;
    private final TransactionService transactionService;
    private final ProductProvider productProvider;

    public PostBidPurchaseNowRes createBidPurchaseNow(
            Long userIdx,
            Long bidPurchaseIdx,
            PostBidPurchaseNowReq postBidPurchaseNowReq
    ) throws BaseException {
        userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        BidPurchase bidPurchase = bidPurchaseRepository
                .findByIdxAndStatus(bidPurchaseIdx, Status.REGISTERING)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BID_NOT_REGISTERING));


        if (!bidPurchase.getUserIdx().equals(userIdx)) throw new BaseException(BaseResponseStatus.BID_NOT_MATCH_USER);

        userProvider.getPaymentCardByIdxAndUserIdxAndStatus(postBidPurchaseNowReq.getCardIdx(), userIdx, Status.ACTIVATED);

        BidSale bidSale = bidSaleRepository
                .findByIdxAndStatus(postBidPurchaseNowReq.getTargetBidSaleIdx(), Status.BIDDING)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TARGET_BID_WRONG_IDX));

        bidSaleRepository.save(bidSale.updateStatus(Status.PROCEEDING));
        bidPurchaseRepository.save(
                bidPurchase.registerCardAndUpdateStatus(postBidPurchaseNowReq.getCardIdx(), Status.PROCEEDING)
                        .updateOrderNo("A-SN1217" + bidPurchaseIdx));

        Transaction transaction = transactionService.createTransaction(bidSale, bidPurchase);

        Payment payment = transactionService.createPayment(
                transaction,
                userIdx,
                bidPurchase.getCardIdx(),
                bidPurchase.getTotalPrice(),
                bidPurchase.getPoint());

        return new PostBidPurchaseNowRes(
                bidPurchase.getIdx(),
                bidSale.getProductSize().getProduct().getThumbnail(),
                payment.getPaymentAmount(),
                bidPurchase.getBidPrice(),
                bidPurchase.getInspectionFee(),
                bidPurchase.getShippingFee(),
                payment.getPointUsed(),
                payment.getStatus()
        );
    }

    public PostBidSaleNowRes createBidSaleNow(
            PostBidSaleNowReq postBidSaleNowReq,
            Long userIdx,
            Long bidSaleIdx
    ) throws BaseException {
        userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);

        BidSale bidSale = bidSaleRepository
                .findByIdxAndStatus(bidSaleIdx, Status.REGISTERING)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BID_NOT_REGISTERING));


        if (!bidSale.getUserIdx().equals(userIdx)) throw new BaseException(BaseResponseStatus.BID_NOT_MATCH_USER);

        userProvider.getPaymentCardByIdxAndUserIdxAndStatus(postBidSaleNowReq.getCardIdx(), userIdx, Status.ACTIVATED);

        BidPurchase bidPurchase = bidPurchaseRepository
                .findByIdxAndStatus(postBidSaleNowReq.getTargetBidPurchaseIdx(), Status.BIDDING)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TARGET_BID_WRONG_IDX));

        bidPurchaseRepository.save(bidPurchase.updateStatus(Status.PROCEEDING));

        bidSaleRepository.save(
                bidSale.updateCardIdxAndModelNo(postBidSaleNowReq.getCardIdx(),"A-SN1217" + bidSaleIdx)
                        .updateStatus(Status.PROCEEDING));

        Transaction transaction = transactionService.createTransaction(bidSale, bidPurchase);

        Payment payment = transactionService.createPayment(
                transaction,
                bidPurchase.getUserIdx(),
                bidPurchase.getCardIdx(),
                bidPurchase.getTotalPrice(),
                bidPurchase.getPoint());

        return new PostBidSaleNowRes(
                bidSale.getIdx(),
                bidSale.getProductSize().getProduct().getThumbnail(),
                bidSale.getSettlementAmount(),
                bidSale.getBidPrice(),
                bidSale.getInspectionFee(),
                bidSale.getShippingFee(),
                bidSale.getAccount().getBank(),
                bidSale.getAccount().getAccount(),
                bidSale.getAccount().getAccountHolder(),
                transaction.getStatus()
        );
    }
    public PostProductPurchaseRes createProductPurchase(
            Long userIdx,
            Long productIdx,
            PostProductPurchaseReq postProductPurchaseReq
    ) throws BaseException{
        userRepository.findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        Product product = productProvider
                .getProductByIdxAndStatus(productIdx,Status.ACTIVATED);

        ProductSize productSize = productProvider
                .getProductSizeByIdxAndProductIdxStatus(postProductPurchaseReq.getProductSizeIdx(),productIdx, Status.ACTIVATED);

        Address address = userProvider
                .getAddressByIdxAndUserIdxAndStatus(postProductPurchaseReq.getAddressIdx(), userIdx,Status.ACTIVATED);

        userProvider.getPaymentCardByIdxAndUserIdxAndStatus(postProductPurchaseReq.getCardIdx(), userIdx,Status.ACTIVATED);

        Long minBidPrice =bidSaleRepository.findMinBidPrice(productSize.getIdx(),Status.BIDDING);

        if(postProductPurchaseReq.getPurchasePrice()>=minBidPrice) throw new BaseException(BaseResponseStatus.BID_PURCHASE_GREATER_THAN_MIN_SALE);

        if (!postProductPurchaseReq.getTotalPrice()
                .equals( postProductPurchaseReq.getPurchasePrice()
                        + postProductPurchaseReq.getInspectionFee()
                        + postProductPurchaseReq.getShippingFee()))
            throw new BaseException(BaseResponseStatus.WRONG_TOTAL_PRICE);


        BidPurchase bidPurchase = bidPurchaseRepository.save(
                BidPurchase.builder()
                        .userIdx(userIdx)
                        .productSize(productSize)
                        .bidPrice(postProductPurchaseReq.getPurchasePrice())
                        .deadline(postProductPurchaseReq.getDeadline())
                        .address(address)
                        .point(postProductPurchaseReq.getPoint())
                        .inspectionFee(postProductPurchaseReq.getInspectionFee())
                        .cardIdx(postProductPurchaseReq.getCardIdx())
                        .shippingFee(postProductPurchaseReq.getShippingFee())
                        .totalPrice(postProductPurchaseReq.getTotalPrice())
                        .status(Status.BIDDING)
                        .build()
        );
        bidPurchaseRepository.save(bidPurchase.updateOrderNo("A-SN1227"+bidPurchase.getIdx()));

        return new PostProductPurchaseRes(
                bidPurchase.getIdx(),
                bidPurchase.getProductSize().getProduct().getThumbnail(),
                bidPurchase.getProductSize().getProduct().getModelNo(),
                bidPurchase.getProductSize().getProduct().getName(),
                bidPurchase.getProductSize().getSize(),
                bidPurchase.getAddress().getName(),
                bidPurchase.getAddress().getAddressDetail(),
                bidPurchase.getAddress().getAddressDetail(),
                bidPurchase.getAddress().getZipCode(),
                bidPurchase.getTotalPrice(),
                bidPurchase.getBidPrice(),
                bidPurchase.getInspectionFee(),
                bidPurchase.getShippingFee(),
                bidPurchase.getPoint(),
                bidPurchase.getCreatedAt().toLocalDate(),
                bidPurchase.getDeadline(),
                bidPurchase.getStatus()
        );
    }

    public PostProductSaleRes createBidSale(PostProductSaleReq postProductSaleReq, Long userIdx, Long productIdx)  throws BaseException{
        userRepository.findByIdxAndStatus(userIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_WRONG_USER_IDX));

        Product product = productProvider
                .getProductByIdxAndStatus(productIdx,Status.ACTIVATED);

        ProductSize productSize = productProvider
                .getProductSizeByIdxAndProductIdxStatus(postProductSaleReq.getProductSizeIdx(),productIdx, Status.ACTIVATED);

        Address address = userProvider
                .getAddressByIdxAndUserIdxAndStatus(postProductSaleReq.getAddressIdx(), userIdx,Status.ACTIVATED);

        Card card= userProvider.getPaymentCardByIdxAndUserIdxAndStatus(postProductSaleReq.getCardIdx(), userIdx,Status.ACTIVATED);

        Account account = userProvider.getAccountByIdxAndUserIdxAndStatus(postProductSaleReq.getAccountIdx(), userIdx,Status.ACTIVATED);

        Long maxBidPrice = bidPurchaseRepository.findMaxBidPrice(postProductSaleReq.getProductSizeIdx(),Status.BIDDING);

        if(postProductSaleReq.getBidPrice()<=maxBidPrice) throw new BaseException(BaseResponseStatus.BID_SALE_LESS_THAN_MAX_BIDN);

        if (!postProductSaleReq.getSettlementAmount()
                .equals( postProductSaleReq.getBidPrice()
                        - postProductSaleReq.getCommission()
                        - postProductSaleReq.getInspectionFee()
                        - postProductSaleReq.getShippingFee()))
            throw new BaseException(BaseResponseStatus.WRONG_TOTAL_PRICE);


        BidSale bidSale = bidSaleRepository.save(
                BidSale.builder()
                        .userIdx(userIdx)
                        .productSize(productSize)
                        .bidPrice(postProductSaleReq.getBidPrice())
                        .deadline(postProductSaleReq.getDeadline())
                        .address(address)
                        .inspectionFee(postProductSaleReq.getInspectionFee())
                        .shippingFee(postProductSaleReq.getShippingFee())
                        .commission(postProductSaleReq.getCommission())
                        .settlementAmount(postProductSaleReq.getSettlementAmount())
                        .account(account)
                        .paymentCardIdx(card.getIdx())
                        .status(Status.BIDDING)
                        .build()
        );

        bidSaleRepository.save(bidSale.updateOrderNum("A-SN3574"+bidSale.getIdx().toString()));


        return new PostProductSaleRes(
                bidSale.getIdx(),
                bidSale.getProductSize().getProduct().getThumbnail(),
                bidSale.getProductSize().getProduct().getName(),
                bidSale.getProductSize().getSize(),
                bidSale.getBidPrice(),
                bidSale.getInspectionFee(),
                bidSale.getShippingFee(),
                bidSale.getCommission(),
                bidSale.getSettlementAmount(),
                bidSale.getCreatedAt().toLocalDate(),
                bidSale.getDeadline(),
                bidSale.getAccount().getBank(),
                bidSale.getAccount().getAccount(),
                bidSale.getAccount().getAccountHolder()
        );

    }
}
