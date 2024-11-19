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
    private final UserService userService;

    @GetMapping("/balance")
    public ResponseEntity<ResponseDto> getBalance(@RequestParam String accountNumber){
        return new ResponseEntity<>(accountService.balanceEnquiry(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/save")
    public ResponseEntity<ResponseDto> saveMoney(@RequestParam String accountNumber, @RequestParam Long amount){
        return new ResponseEntity<>(accountService.saveToAccount(accountNumber, amount), HttpStatus.OK);
    }

    @GetMapping("/withdraw")
    public ResponseEntity<ResponseDto> withdrawMoney(@RequestParam String accountNumber, @RequestParam Long amount){
        return new ResponseEntity<>(accountService.withdrawFromAccount(accountNumber, amount), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<ResponseDto> getTransactions(@RequestParam String accountNumber){
        return new ResponseEntity<>(accountService.getUserTransactions(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<AppUser>> getAllUser(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<String>> deleteUserById(@PathVariable Long userId){
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);

    }

    @PutMapping("/account/deactivate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> deactivateAccount(@RequestParam Long userId, @RequestParam Long accountId){
        return new ResponseEntity<>(accountService.deactivateUserAccount(userId,accountId), HttpStatus.OK);
    }

    @PutMapping("/account/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> activateAccount(@RequestParam Long userId, @RequestParam Long accountId){
        return new ResponseEntity<>(accountService.activateAccount(userId, accountId), HttpStatus.OK);
    }

    @GetMapping("/account/getActiveAccountsList")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> activeAccountList(){
        return new ResponseEntity<>(accountService.getAllActiveAccountList(), HttpStatus.OK);
    }

    @GetMapping("/account/getInActiveAccountsList")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto>inActiveAccountList(){
        return new ResponseEntity<>(accountService.getAllInActiveAccountList(), HttpStatus.OK);

    }

    @GetMapping("/accountList/ByAccountType/{accType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> getAccountListByAccType(@PathVariable AccountType accType){
        return new ResponseEntity<>(accountService.byAccType(accType), HttpStatus.OK);
    }


    /*
    Users should be able to view their account balances and transaction history.
            • Implement functionalities for transferring funds between accounts.
            • Ensure that transactions are processed securely and accurately.*/
}
