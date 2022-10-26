package com.example.demo.src.transaction.model;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.Status;
import com.example.demo.src.product.model.entity.Product;
import com.example.demo.src.product.model.entity.ProductRepository;
import com.example.demo.src.product.model.entity.ProductSizeRepository;
import com.example.demo.src.transaction.model.dto.GetTransactionListRes;
import com.example.demo.src.transaction.model.entity.Transaction;
import com.example.demo.src.transaction.model.entity.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionProvider {
    private final TransactionRepository transactionRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductRepository productRepository;

    public GetTransactionListRes retrieveTransactionList(
            Long productIdx,
            Long productSizeIdx,
            Integer duration,
            Pageable pageable
    ) throws BaseException {
        Product product = productRepository
                .findByIdxAndStatus(productIdx, Status.ACTIVATED)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PRODUCT_WRONG_IDX));

        Page<Transaction> transactionList;

        LocalDateTime referenceDate = null;
        if (duration != null) {
            referenceDate = LocalDateTime.now().minusMonths(duration);
        }
        if (productSizeIdx == null) {
            List<Long> productSizeList = product.getProductSizes().stream()
                    .map(entity -> entity.getIdx()).collect(Collectors.toList());
            if (referenceDate != null) {
                transactionList = transactionRepository
                        .findByProductSizeIdxInAndCreatedAtGreaterThanEqualAndStatus(
                                productSizeList,
                                referenceDate,
                                Status.DELIVERY_COMPLETED,
                                pageable);
            } else {
                transactionList = transactionRepository
                        .findByProductSizeIdxInAndStatus(
                                productSizeList,
                                Status.DELIVERY_COMPLETED,
                                pageable);
            }
        } else {
            if (!productSizeRepository
                    .existsByProductIdxAndIdxAndStatus(
                            productIdx,
                            productSizeIdx,
                            Status.ACTIVATED
                    ))
                throw new BaseException(BaseResponseStatus.PRODUCT_SIZE_WRONG_IDX);
            if (referenceDate != null) {
                transactionList = transactionRepository
                        .findByProductSizeIdxAndCreatedAtGreaterThanEqualAndStatus(
                                productSizeIdx,
                                referenceDate,
                                Status.DELIVERY_COMPLETED,
                                pageable);
            } else {

                transactionList = transactionRepository
                        .findByProductSizeIdxAndStatus(
                                productSizeIdx,
                                Status.DELIVERY_COMPLETED,
                                pageable);
            }
        }
        return new GetTransactionListRes(transactionList);
    }
}
