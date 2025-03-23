package com.achawathe.Banking.project;

import com.achawathe.Banking.project.domain.dto.AccountDto;
import com.achawathe.Banking.project.domain.dto.UserDto;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;

import java.math.BigDecimal;
import java.util.UUID;

public final class TestDataUtil {

    private TestDataUtil(){

    }

    public static UserEntity createUserEntityA() {
        return UserEntity
                .builder()
                .name("Arthur")
                .build();
    }

    public static UserEntity createUserEntityB() {
        return UserEntity
                .builder()
                .name("Jeremy")
                .build();
    }

    public static UserEntity createUserEntityC() {
        return UserEntity
                .builder()
                .name("Carlos")
                .build();
    }

    public static UserDto createUserDtoA() {
        return UserDto
                .builder()
                .name("Arthur")
                .build();
    }



    public static AccountEntity createAccountEntityA(UserEntity user) {
        return AccountEntity.builder()
                .user(user)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    public static AccountDto createAccountDtoA(UserDto user) {
        return AccountDto.builder()
                .user(user)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    public static AccountEntity createAccountEntityA2(UserEntity user) {
        return AccountEntity.builder()
                .user(user)
                .balance(BigDecimal.valueOf(3000))
                .build();
    }

    public static AccountEntity createAccountEntityB(UserEntity user) {
        return AccountEntity.builder()
                .user(user)
                .balance(BigDecimal.valueOf(2000))
                .build();
    }

    public static AccountEntity createAccountEntityB2(UserEntity user) {
        return AccountEntity.builder()
                .user(user)
                .balance(BigDecimal.valueOf(4000))
                .build();
    }

    public static TransactionEntity createTransactionEntityforSameAccount(AccountEntity accountEntityA, BigDecimal amount, TransactionEntity.TransactionType transactionType) {
        return TransactionEntity.builder()
                .transactionType(transactionType)
                .accountFrom(accountEntityA)
                .accountTo(accountEntityA)
                .user(accountEntityA.getUser())
                .amount(amount)
                .build();
    }

    public static TransactionEntity createTransactionEntityForDifferentAccount(AccountEntity accountEntityA,AccountEntity accountEntityB, BigDecimal amount, TransactionEntity.TransactionType transactionType) {
        return TransactionEntity.builder()
                .transactionType(transactionType)
                .accountFrom(accountEntityA)
                .accountTo(accountEntityB)
                .user(accountEntityA.getUser())
                .amount(amount)
                .build();
    }


}
