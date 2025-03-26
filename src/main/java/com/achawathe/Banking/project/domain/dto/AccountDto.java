package com.achawathe.Banking.project.domain.dto;

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
public class AccountDto {
    private Long id;

    @Schema(defaultValue = "{name : \"Name\"}")
    private UserDto user;

    private String accountNumber;

    @Schema(defaultValue = "0")
    private BigDecimal balance;
}
