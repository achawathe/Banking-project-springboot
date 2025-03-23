package com.achawathe.Banking.project.services;

import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


public interface TransactionService {
    List<TransactionEntity> findAllByUser(UserEntity user);
    List<TransactionEntity> findAllByAccountFromOrAccountTo(AccountEntity accountFrom, AccountEntity accountTo);
    List<TransactionEntity> findAllByAccountFromAndAccountTo(AccountEntity accountFrom, AccountEntity accountTo);
    List<TransactionEntity> findAllByTransactionType(TransactionEntity.TransactionType transactionType);
    List<TransactionEntity> findAllByTransactionTypeAndUser(TransactionEntity.TransactionType transactionType, UserEntity user);
    List<TransactionEntity> findAll();
    TransactionEntity save(TransactionEntity transactionEntity);
    void delete(TransactionEntity transactionEntity);
    TransactionEntity update(TransactionEntity transactionEntity);
    boolean existsById(String id);
    boolean validTransaction(TransactionEntity transactionEntity);
    Optional<TransactionEntity> getById(String id);
}
