package com.achawathe.Banking.project.domain.dto;


import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    private Long id;

    @Schema(defaultValue = "acc1")
    private AccountDto accountFrom;

    @Schema(defaultValue = "acc2")
    private AccountDto accountTo;

    @Schema(defaultValue = "user_name")
    private UserDto user;

    @Schema(defaultValue = "0")
    private BigDecimal amount;

    @Schema(defaultValue = "TransactionType.TRANSFER")
    private TransactionType transactionType;

    private enum TransactionType{DEPOSIT, WITHDRAWAL, TRANSFER, OPENING, CLOSING};

}
