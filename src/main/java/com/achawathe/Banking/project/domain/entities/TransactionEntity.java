package com.achawathe.Banking.project.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_from_id")
    private AccountEntity accountFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_to_id")
    private AccountEntity accountTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // Fixed naming
    private UserEntity user;

//    @Version
//    private Integer version;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public enum TransactionType { DEPOSIT, WITHDRAWAL, TRANSFER, OPENING, CLOSING }
}
