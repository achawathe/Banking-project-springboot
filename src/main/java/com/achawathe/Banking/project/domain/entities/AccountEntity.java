package com.achawathe.Banking.project.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class AccountEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(defaultValue = "{name : \"Name\"}")
    private UserEntity user;


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountNumber;

    @Schema(defaultValue = "0")
    private BigDecimal balance;
}
