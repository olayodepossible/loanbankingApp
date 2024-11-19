package com.possible.loanbanking.service;

import com.possible.loanbanking.dto.req.AccountType;
import com.possible.loanbanking.dto.req.TransactionDto;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.model.Account;
import com.possible.loanbanking.model.SavingsAccount;
import com.possible.loanbanking.repository.UserRepository;
import com.possible.loanbanking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface AccountService {

  Account createSavingsAccount(Long customerId);

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


}

