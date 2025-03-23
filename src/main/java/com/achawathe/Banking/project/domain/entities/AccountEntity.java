package com.achawathe.Banking.project.domain.entities;

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
    private UserEntity user;

//    @Version
//    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountNumber;

    private BigDecimal balance;
}
