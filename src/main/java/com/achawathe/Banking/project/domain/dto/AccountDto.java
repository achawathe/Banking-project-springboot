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
    @Schema(hidden = true)
    private Long id;

    @Schema(defaultValue = "{name : \"Name\"}")
    private UserDto user;

    @Schema(hidden = true)
    private String accountNumber;

    @Schema(defaultValue = "0")
    private BigDecimal balance;

    @Schema(defaultValue = "false", hidden = true)
    private boolean isDeleted = false;
}
