package com.achawathe.Banking.project.controllers;


import com.achawathe.Banking.project.domain.dto.AccountDto;
import com.achawathe.Banking.project.domain.dto.TransactionDto;
import com.achawathe.Banking.project.domain.dto.UserDto;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.mappers.Mapper;
import com.achawathe.Banking.project.services.AccountService;
import com.achawathe.Banking.project.services.TransactionService;
import com.achawathe.Banking.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    private AccountService accountService;

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;
    private Mapper<AccountEntity, AccountDto> accountMapper;
    private Mapper<TransactionEntity, TransactionDto> transactionMapper;

    TransactionController (TransactionService transactionService, AccountService accountService, UserService userService, Mapper<UserEntity, UserDto> userMapper,Mapper<AccountEntity, AccountDto> accountMapper, Mapper<TransactionEntity, TransactionDto> transactionMapper){
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping(value = "/transactions")
    public List<TransactionDto> getTransactions(){

        List<TransactionEntity> transactions = transactionService.findAll();
        //Edge case safety not important; front end would handle this issue
        return transactions.stream()
                .map(transactionMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable String id) {
        Optional<TransactionEntity> transaction;
        transaction = transactionService.getById(id);
        if(transaction.isPresent()){
            return new ResponseEntity<>(transactionMapper.mapTo(transaction.orElse(null)), HttpStatus.OK);
        }
        return new ResponseEntity<>("Transaction Does Not Exist",HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/transactions/user/{user_id}")
    public ResponseEntity<?> getTransactionByUserId(@PathVariable String user_id){
        Optional<UserEntity> user;
        user = userService.getUser(user_id);
        //assume no user exists with null id
        if(user.isEmpty()){
            return new ResponseEntity<>("User is empty",HttpStatus.BAD_REQUEST);
        }
        // user may exist, but may not have an account.
        if(accountService.getAccountByUser(user.orElse(null)).isEmpty()){
            return new ResponseEntity<>("User Exists, but does not have account or user provided is invalid.",HttpStatus.BAD_REQUEST);
        }

        List<TransactionEntity> transactionslist = transactionService.findAllByUser(user.orElse(null));
        //if a user has at least one account, it is safe to assume they will have at minimum one transaction. in the case they don't, the front end would handle the error.
        return new ResponseEntity<>(transactionslist.stream().map(transactionMapper::mapTo).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/transactions/account/{account_id}")
    public ResponseEntity<?> getTransactionByAccountId(@PathVariable UUID account_id){
        Optional<AccountEntity> account;
        account = accountService.findByAccountNumber(account_id);
        //assume no account exists with null id
        if(account.isEmpty()){
            return new ResponseEntity<>("Account Does Not Exist.",HttpStatus.BAD_REQUEST);
        }
        List<TransactionEntity> transactionEntities = transactionService.findAllByAccountFromOrAccountTo(account.get(), account.get());
        //assume at accounts have at least one transaction. in case they don't front end would take care of empty list.
        return new ResponseEntity<>(transactionEntities.stream().map(transactionMapper::mapTo).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/transactions/accToAndFrom/{account_id1}/{account_id2}")
    public ResponseEntity<?> getTransactionsByAccountId1AndAccountId2(@PathVariable UUID account_id1, @PathVariable UUID account_id2){
        Optional<AccountEntity> accountTo = accountService.findByAccountNumber(account_id1);
        Optional<AccountEntity> accountFrom = accountService.findByAccountNumber(account_id2);
        if(accountTo.isPresent() && accountFrom.isPresent()){
            List<TransactionEntity> transactionEntities = transactionService.findAllByAccountFromAndAccountTo(accountFrom.get(), accountTo.get());
            return new ResponseEntity<>(transactionEntities.stream().map(transactionMapper::mapTo).collect(Collectors.toList()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Account to and/or Account from not found.",HttpStatus.BAD_REQUEST);
    }

    //Transaction between accounts with same user
    @PutMapping(value = "/transaction/transfer/{account_id1}_to_{account_id2}")
    public ResponseEntity<?> transferTransaction(@PathVariable String account_id1, @PathVariable String account_id2, @RequestBody double amountIn){
        BigDecimal amount = new BigDecimal(amountIn);

        Optional<AccountEntity> accountTo = accountService.findByAccountNumber(UUID.fromString(account_id2));
        Optional<AccountEntity> accountFrom = accountService.findByAccountNumber(UUID.fromString(account_id1));
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(TransactionEntity.TransactionType.TRANSFER);
        transaction.setAccountFrom(accountFrom.orElse(null));
        transaction.setAccountTo(accountTo.orElse(null));
        transaction.setAmount(amount);
        transaction.setUser(accountFrom.get().getUser());
        if(transactionService.validTransaction(transaction) && accountTo.isPresent()){
            accountFrom.get().setBalance(accountFrom.get().getBalance().subtract(amount));
            accountTo.get().setBalance(accountTo.get().getBalance().add(amount));
            transactionService.save(transaction);
            return new ResponseEntity<>(transactionMapper.mapTo(transaction),HttpStatus.OK);
        }
        return new ResponseEntity<>("Transaction given was not valid.",HttpStatus.BAD_REQUEST);
    }

    //Withdraw
    @PutMapping(value = "/transaction/withdraw/{account_id}")
    public ResponseEntity<?> withdrawTransaction(@PathVariable UUID account_id, @RequestBody BigDecimal amount){
        Optional<AccountEntity> account = accountService.findByAccountNumber(account_id);
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccountFrom(account.get());
        transaction.setAccountTo(account.get());
        transaction.setAmount(amount);
        transaction.setUser(account.get().getUser());
        if(transactionService.validTransaction(transaction)){
            account.get().setBalance(account.get().getBalance().subtract(amount));
            TransactionEntity savedTransaction = transactionService.save(transaction);
            TransactionDto transactionDto = transactionMapper.mapTo(savedTransaction);
            return new ResponseEntity<>(transactionDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Withdrawal not valid", HttpStatus.BAD_REQUEST);
    }

    //Deposit
    @PutMapping(value = "/transaction/deposit/{account_id}")
    public ResponseEntity<?> depositTransaction(@PathVariable UUID account_id, @RequestBody BigDecimal amount){
        Optional<AccountEntity> account = accountService.findByAccountNumber(account_id);
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccountFrom(account.get());
        transaction.setAccountTo(account.get());
        transaction.setAmount(amount);
        transaction.setUser(account.get().getUser());
        if(transactionService.validTransaction(transaction)){
            account.get().setBalance(account.get().getBalance().add(amount));
            TransactionEntity savedTransaction = transactionService.save(transaction);
            TransactionDto transactionDto = transactionMapper.mapTo(savedTransaction);
            return new ResponseEntity<>(transactionDto,HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid deposit",HttpStatus.BAD_REQUEST);
    }

}
