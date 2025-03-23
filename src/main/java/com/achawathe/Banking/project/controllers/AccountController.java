package com.achawathe.Banking.project.controllers;
import com.achawathe.Banking.project.domain.dto.AccountDto;
import com.achawathe.Banking.project.domain.dto.UserDto;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.mappers.Mapper;
import com.achawathe.Banking.project.services.AccountService;
import com.achawathe.Banking.project.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.achawathe.Banking.project.services.TransactionService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    private AccountService accountService;
    private UserService userService;
    private TransactionService transactionService;
    private Mapper<UserEntity,UserDto> userMapper;

    private Mapper<AccountEntity,AccountDto> accountMapper;


    //constructor
    public AccountController(AccountService accountService, UserService userService, Mapper<AccountEntity,AccountDto> accountMapper, Mapper<UserEntity,UserDto> userMapper,TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.accountMapper = accountMapper;
        this.userMapper = userMapper;
        this.transactionService = transactionService;
    }

    //create account
    @Tag(name = "post", description = "POST method to create Account")
    @PostMapping(value = "/create-account")
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) {

        AccountEntity accountEntity = accountMapper.mapFrom(accountDto);

        //check for validity
        if (!accountService.validateAccount(accountEntity)) {
            return new ResponseEntity<>("Account provided has incomplete information, or is improperly formatted",HttpStatus.BAD_REQUEST);
        }

        UserEntity user = accountEntity.getUser();
        if (user.getId() == null && ! userService.userExists(accountEntity.getUser().getName())) {
            user = userService.save(user);
            accountEntity.setUser(user);
        } else if (userService.userExists(accountEntity.getUser().getName())) {
            accountEntity.setUser(userService.getUser(accountEntity.getUser().getName()).get());
            user = userService.getUser(accountEntity.getUser().getName()).get();
        }

        // **Save the account before creating transactions**
        accountEntity = accountService.save(accountEntity);

        // **Create and persist the transaction AFTER the account is persisted**
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .accountFrom(accountEntity)
                .accountTo(accountEntity)
                .transactionType(TransactionEntity.TransactionType.OPENING)
                .amount(accountEntity.getBalance())
                .user(user)
                .build();

        transactionService.save(transactionEntity);

        return new ResponseEntity<>(accountMapper.mapTo(accountEntity), HttpStatus.CREATED);
    }

    //get all accounts assigned to every user
    @Tag(name = "get", description = "GET method to get all accounts")
    @GetMapping(value = "/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        List<AccountEntity> accounts = accountService.getAllAccounts();
        //Edge case safety not important; front end would handle this issue
        return new ResponseEntity<>(accounts.stream()
                .map(accountMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    //get all accounts owned by a single user
    @Tag(name = "get", description = "GET method to get account by User ID")
    @GetMapping(value = "/accounts/user/{id}")
    public ResponseEntity<?> getAccounts(@PathVariable Long id) {
        if(userService.userExists(id)) {
            UserDto user = userMapper.mapTo(userService.getUser(id).orElse(null));


            List<AccountEntity> accounts = accountService.getAccountByUser(userMapper.mapFrom(user));
            return  new ResponseEntity<>(accounts.stream()
                    .map(accountMapper::mapTo)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot Find User",HttpStatus.NOT_FOUND);
    }

    //get an account by account number
    @Tag(name = "get", description = "GET method to get account by Account Number")
    @GetMapping(value = "/accounts/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
         UUID accountUUID = UUID.fromString(accountNumber);

        if(accountService.accountExists(accountUUID)) {
            AccountDto accountDto = accountMapper.mapTo(accountService.findByAccountNumber(accountUUID).orElse(null));

            return accountService.validateAccount(accountMapper.mapFrom(accountDto))? new ResponseEntity<>(accountDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Account Number Not Found",HttpStatus.NOT_FOUND);
    }


    // close Account
    @Tag(name = "delete", description = "DELETE method to close account by account number")
    @DeleteMapping(value = "/accounts/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID accountNumber) {
        if(accountService.accountExists(accountNumber)) {
            AccountEntity accountEntity = accountService.findByAccountNumber(accountNumber).orElseThrow();

            UserEntity user = accountEntity.getUser();
            if (user.getId() == null && ! userService.userExists(accountEntity.getUser().getName())) {
                user = userService.save(user);
                accountEntity.setUser(user);
            } else if (userService.userExists(accountEntity.getUser().getName())) {
                accountEntity.setUser(userService.getUser(accountEntity.getUser().getName()).get());
                user = userService.getUser(accountEntity.getUser().getName()).get();
            }

            // **Save the account before creating transactions**
            accountEntity = accountService.save(accountEntity);

            // **Create and persist the transaction AFTER the account is persisted**
            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .accountFrom(accountEntity)
                    .accountTo(accountEntity)
                    .transactionType(TransactionEntity.TransactionType.OPENING)
                    .amount(accountEntity.getBalance())
                    .user(user)
                    .build();

            transactionService.save(transactionEntity);

            return new ResponseEntity<>("Account Deleted Successfully",HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>("Account Not Found",HttpStatus.NOT_FOUND);
    }
}
