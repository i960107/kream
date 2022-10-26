package com.example.demo.src.transaction.model;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.bid.BidProvider;
import com.example.demo.src.bid.purchase.model.entity.BidPurchase;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import com.example.demo.src.transaction.model.entity.Payment;
import com.example.demo.src.transaction.model.entity.PaymentRepository;
import com.example.demo.src.transaction.model.entity.Transaction;
import com.example.demo.src.transaction.model.entity.TransactionRepository;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.entity.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final BidProvider bidProvider;
    private final UserService userService;
    private final UserProvider userProvider;


    public Transaction createTransaction(BidSale bidSale, BidPurchase bidPurchase) throws BaseException {
        bidProvider.isBidSaleExist(bidSale.getIdx(), Status.PROCEEDING);
        bidProvider.isBidPurchaseExist(bidPurchase.getIdx(), Status.PROCEEDING);

        if(bidSale.getProductSize()!=bidPurchase.getProductSize())
            throw new BaseException(BaseResponseStatus.BID_NOT_MATCH_PRODUCT_SIZE);
        if(!bidSale.getBidPrice() .equals( bidPurchase.getBidPrice()))
            throw new BaseException(BaseResponseStatus.PRICE_NOT_MATCH_BID_PRICE);

        Transaction transaction = transactionRepository.save(
                Transaction.builder()
                        .bidSaleIdx(bidSale.getIdx())
                        .bidPurchaseIdx(bidPurchase.getIdx())
                        .productSize(bidSale.getProductSize())
                        .winningBidPrice(bidSale.getBidPrice())
                        .status(Status.BEFORE_PAYMENT)
                        .build());
        return transaction;
    }

    public Payment createPayment(Transaction transaction, Long userIdx, Long cardIdx, Long totalPrice,String point) throws BaseException{
        User user =userProvider.getUserByIdxAndStatus(userIdx, Status.ACTIVATED);
        Long pointUsed =0L;
        if(point.equals("TRUE")) {
         pointUsed = userService.useAllPoint(user, totalPrice);
        }

        isTransactionExist(transaction.getIdx(),Status.BEFORE_PAYMENT);

        bidProvider.isPaymentInfoValid(transaction.getBidPurchaseIdx(),cardIdx,totalPrice,Status.PROCEEDING);
        //빌링키로 pg사에 결제 요청후 응답 결과에 따라 처리해주기.

        transactionRepository.save(transaction.updateStatus(Status.PAYMENT_COMPLETED));

        return paymentRepository.save(
                Payment.builder()
                        .transactionIdx(transaction.getIdx())
                        .user(user)
                        .cardIdx(cardIdx)
                        .totalPrice(totalPrice)
                        .pointUsed(pointUsed)
                        .paymentAmount(totalPrice-pointUsed)
                        .status(Status.PAYMENT_SUCCESS)
                        .build());
    }

    public boolean isTransactionExist(Long transactionIdx, Status status)throws BaseException{
        return  (transactionRepository.findByIdxAndStatus(transactionIdx,status)
                .orElseThrow(()->new BaseException(BaseResponseStatus.TRANSACTION_NOT_BEFORE_PAYMENT))!=null);
    }
}
