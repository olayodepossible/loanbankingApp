package com.possible.loanbanking.service.impl;

import com.possible.loanbanking.dto.req.AccountType;
import com.possible.loanbanking.dto.req.AppUser;
import com.possible.loanbanking.dto.req.TransactionDto;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.exceptiion.AccountNotFoundException;
import com.possible.loanbanking.model.Account;
import com.possible.loanbanking.model.Transaction;
import com.possible.loanbanking.repository.AccountRepository;
import com.possible.loanbanking.repository.TransactionRepository;
import com.possible.loanbanking.repository.UserRepository;
import com.possible.loanbanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private static final String ACCT_NOT_FOUND = "Account with number %s not found";
    @Override
    public ResponseDto balanceEnquiry(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException(String.format(ACCT_NOT_FOUND, accountNumber)));

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("User balance retrieved successfully")
                .data(account.getBalance())
                .build();
    }

    @Override
    public Account createSavingsAccount(Long customerId) {
        return null;
    }

    @Override
    public ResponseDto getUserTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException(String.format("Account with Account number %s not found", accountNumber)));
        List<Transaction> transactions = transactionRepository.findAllTransactionByAccountId(account.getId());
        if (transactions.isEmpty()){
            return ResponseDto.builder()
                    .statusCode(200)
                    .responseMessage("User Transaction history is empty")
                    .data(transactions)
                    .build();
        }
        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("User Transactions retrieved successfully")
                .data(transactions)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto creditOrDebitAccountTransaction(TransactionDto transactionDto) {
        Account initiator = accountRepository.findByAccountNumber(transactionDto.getInitiatorAccount())
                .orElseThrow(() -> new AccountNotFoundException(String.format(ACCT_NOT_FOUND, transactionDto.getInitiatorAccount())));

        Account beneficiary = accountRepository.findByAccountNumber(transactionDto.getBeneficiaryAccount())
                .orElseThrow(() -> new AccountNotFoundException(String.format(ACCT_NOT_FOUND, transactionDto.getBeneficiaryAccount())));

        BigDecimal tranxAmount = transactionDto.getAmount();
        BigDecimal acctToCreditBalance = beneficiary.getBalance().add(tranxAmount);


        Transaction transaction;
        String message = "Insufficient Balance";
        if (transactionDto.getTransactionType().equalsIgnoreCase("DEBIT")) {
            BigDecimal previousBalance = initiator.getBalance();
            if ( previousBalance.compareTo(tranxAmount) > 0){
                BigDecimal availableBalance = initiator.getBalance().subtract(tranxAmount);
                transaction = Transaction.builder()
                        .account(initiator)
                        .credit(BigDecimal.ZERO)
                        .debit(tranxAmount)
                        .amount(tranxAmount)
                        .balance(availableBalance)
                        .beneficiaryAcct(transactionDto.getBeneficiaryAccount())
                        .senderAcct(transactionDto.getInitiatorAccount())
                        .narration(transactionDto.getDescription())
                        .build();
                initiator.setBalance(availableBalance);
                message = String.format("Credit Account number %s and Debit Account number %s",transactionDto.getBeneficiaryAccount(),  transactionDto.getInitiatorAccount());
            }
            else {
                return ResponseDto.builder()
                        .statusCode(200)
                        .responseMessage(message)
                        .data(List.of())
                        .build();
            }

        }
        else {
            transaction = Transaction.builder()
                    .account(initiator)
                    .credit(tranxAmount)
                    .debit(BigDecimal.ZERO)
                    .amount(tranxAmount)
                    .balance(acctToCreditBalance)
                    .beneficiaryAcct(transactionDto.getBeneficiaryAccount())
                    .senderAcct(transactionDto.getInitiatorAccount())
                    .narration(transactionDto.getDescription())
                    .build();

            initiator.setBalance(acctToCreditBalance);
            message = String.format("Credit Account number %s and Debit Account number %s",transactionDto.getBeneficiaryAccount(),  transactionDto.getInitiatorAccount());

        }

        accountRepository.save(initiator);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage(message)
                .data(savedTransaction)
                .build();
    }

    @Override
    public ResponseDto activateAccount(Long userId, Long accountId) {
        AppUser user = userRepository.findById(userId).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if(user.getAccountList().contains(account)){
            account.setStatus("ACTIVE");
            Account updatedAcct = accountRepository.save(account);
            return ResponseDto.builder()
                    .statusCode(200)
                    .responseMessage("Deactivated Account for User with id: "+userId)
                    .data(updatedAcct)
                    .build();
        }

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("Error deactivating Account for User with id: "+userId)
                .data("")
                .build();
    }

    @Override
    public ResponseDto deactivateUserAccount(Long userId, Long accountId) {
        AppUser user = userRepository.findById(userId).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if(user.getAccountList().contains(account)){
            account.setStatus("INACTIVE");
            Account updatedAcct = accountRepository.save(account);
            return ResponseDto.builder()
                    .statusCode(200)
                    .responseMessage("Deactivated Account for User with id: "+userId)
                    .data(updatedAcct)
                    .build();
        }

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("Error deactivating Account for User with id: "+userId)
                .data("")
                .build();
    }

    @Override
    public ResponseDto saveToAccount(String accountNumber, Long amount) {
        Account initiator = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(ACCT_NOT_FOUND, accountNumber)));
        BigDecimal acctToCreditBalance = initiator.getBalance().add(BigDecimal.valueOf(amount));

        Transaction transaction = Transaction.builder()
                .account(initiator)
                .credit(BigDecimal.valueOf(amount))
                .debit(BigDecimal.ZERO)
                .amount(BigDecimal.valueOf(amount))
                .balance(acctToCreditBalance)
                .beneficiaryAcct(accountNumber)
                .senderAcct(accountNumber)
                .narration("Credit account by self")
                .build();

        initiator.setBalance(acctToCreditBalance);
        String message = String.format("Credit Account number %s successfully",  accountNumber);
        accountRepository.save(initiator);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage(message)
                .data(savedTransaction)
                .build();
    }

    @Override
    public ResponseDto withdrawFromAccount(String accountNumber, Long amount) {

        Account initiator = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(ACCT_NOT_FOUND, accountNumber)));

        BigDecimal tranxAmount = BigDecimal.valueOf(amount);
        String message = "Insufficient Balance";
        BigDecimal previousBalance = initiator.getBalance();
        if ( previousBalance.compareTo(tranxAmount) > 0){
            BigDecimal availableBalance = initiator.getBalance().subtract(tranxAmount);
            Transaction transaction = Transaction.builder()
                    .account(initiator)
                    .credit(BigDecimal.ZERO)
                    .debit(tranxAmount)
                    .amount(tranxAmount)
                    .balance(availableBalance)
                    .beneficiaryAcct(accountNumber)
                    .senderAcct(accountNumber)
                    .narration("Debit account by self")
                    .build();
            initiator.setBalance(availableBalance);
            message = String.format("Debit Account number %s", accountNumber);

            accountRepository.save(initiator);
            Transaction savedTransaction = transactionRepository.save(transaction);

            return ResponseDto.builder()
                    .statusCode(200)
                    .responseMessage(message)
                    .data(savedTransaction)
                    .build();
        }

        return ResponseDto.builder()
                .statusCode(400)
                .responseMessage(message)
                .data(null)
                .build();
    }


    @Override
    public ResponseDto getAllActiveAccountList() {
        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("List of Accounts fetched successfully")
                .data(accountRepository.findAllActiveAccounts())
                .build();

    }

    @Override
    public ResponseDto getAllInActiveAccountList() {

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("List of Accounts fetched successfully")
                .data(accountRepository.findAllInActiveAccounts())
                .build();

    }


    @Override
    public ResponseDto byAccType(AccountType accType) {

        return ResponseDto.builder()
                .statusCode(200)
                .responseMessage("List of Accounts fetched successfully")
                .data(accountRepository.findAllByAccountType(accType))
                .build();
    }



    public Account createSavingsAccount2(Long customerId) {
        if (!userRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer does not exist.");
        }
        if (accountRepository.findByUser(customerId).isPresent()) {
            throw new IllegalArgumentException("Customer already has a savings account.");
        }
        String accountNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
       Account account = new Account();
        account.setUser(userRepository.findById(customerId).orElseThrow());
        account.setAccountNumber(accountNumber);
        return accountRepository.save(account);
    }
}
