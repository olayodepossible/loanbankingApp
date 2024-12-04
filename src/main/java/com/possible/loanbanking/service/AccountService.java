package com.possible.loanbanking.service;

import com.possible.loanbanking.dto.req.AccountType;
import com.possible.loanbanking.dto.req.TransactionDto;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.model.Transaction;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;


public interface AccountService {



    ResponseDto getUserTransactions(String accountNumber);

    ResponseDto  balanceEnquiry(String accountNumber);
    ResponseDto creditOrDebitAccountTransaction(TransactionDto transactionDto);

    ResponseDto byAccType(AccountType accType);

    ResponseDto getAllInActiveAccountList();

    ResponseDto getAllActiveAccountList();

    ResponseDto activateAccount(Long userId, Long accountId);

    ResponseDto deactivateUserAccount(Long userId, Long accountId);


    ResponseDto saveToAccount(String accountNumber, Long amount);

    ResponseDto withdrawFromAccount(String accountNumber, Long amount);


    Page<Transaction> getAccountStatement(String accountNumber, LocalDateTime start, LocalDateTime end, Pageable pageable);
}

