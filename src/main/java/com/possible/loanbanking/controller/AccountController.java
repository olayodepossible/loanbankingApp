package com.possible.loanbanking.controller;

import com.possible.loanbanking.dto.req.AccountType;
import com.possible.loanbanking.dto.req.AppUser;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.service.AccountService;
import com.possible.loanbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<ResponseDto<Object>> getBalance(@RequestParam String accountNumber){
        return new ResponseEntity<>(accountService.balanceEnquiry(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/save")
    public ResponseEntity<ResponseDto<Object>> saveMoney(@RequestParam String accountNumber, @RequestParam Long amount){
        return new ResponseEntity<>(accountService.saveToAccount(accountNumber, amount), HttpStatus.OK);
    }

    @GetMapping("/withdraw")
    public ResponseEntity<ResponseDto<Object>> withdrawMoney(@RequestParam String accountNumber, @RequestParam Long amount){
        return new ResponseEntity<>(accountService.withdrawFromAccount(accountNumber, amount), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<ResponseDto<Object>> getTransactions(@RequestParam String accountNumber){
        return new ResponseEntity<>(accountService.getUserTransactions(accountNumber), HttpStatus.OK);
    }

    @PutMapping("/account/deactivate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> deactivateAccount(@RequestParam Long userId, @RequestParam Long accountId){
        return new ResponseEntity<>(accountService.deactivateUserAccount(userId,accountId), HttpStatus.OK);
    }

    @PutMapping("/account/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> activateAccount(@RequestParam Long userId, @RequestParam Long accountId){
        return new ResponseEntity<>(accountService.activateAccount(userId, accountId), HttpStatus.OK);
    }

    @GetMapping("/account/getActiveAccountsList")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> activeAccountList(){
        return new ResponseEntity<>(accountService.getAllActiveAccountList(), HttpStatus.OK);
    }

    @GetMapping("/account/getInActiveAccountsList")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>>inActiveAccountList(){
        return new ResponseEntity<>(accountService.getAllInActiveAccountList(), HttpStatus.OK);

    }

    @GetMapping("/accountList/ByAccountType/{accType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> getAccountListByAccType(@PathVariable AccountType accType){
        return new ResponseEntity<>(accountService.byAccType(accType), HttpStatus.OK);
    }

}
