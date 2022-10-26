package com.example.demo.src.transaction.model;

import com.example.demo.config.Auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Role;
import com.example.demo.src.transaction.model.dto.GetTransactionListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionProvider transactionProvider;
    private final TransactionService transactionService;



}
