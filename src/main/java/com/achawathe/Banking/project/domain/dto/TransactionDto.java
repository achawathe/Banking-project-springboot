package com.achawathe.Banking.project.domain.dto;


import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

    private Long id;

    private AccountDto accountFrom;

    private AccountDto accountTo;

    private UserDto user;

    private BigDecimal amount;

    private TransactionType transactionType;

    private enum TransactionType{DEPOSIT, WITHDRAWAL, TRANSFER, OPENING, CLOSING};

}
