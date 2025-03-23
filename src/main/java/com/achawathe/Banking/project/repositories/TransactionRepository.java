package com.achawathe.Banking.project.repositories;

import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String>,
        PagingAndSortingRepository<TransactionEntity, String> {

    // Fetch all transactions for a specific account
    // Finds transactions where an account is either sender or receiver
    Iterable<TransactionEntity> findAllByAccountFromOrAccountTo(AccountEntity accountFrom, AccountEntity accountTo);

    Iterable<TransactionEntity> findAllByUser(UserEntity user);

    Iterable<TransactionEntity> findAllByAccountFromAndAccountTo(AccountEntity accountFrom, AccountEntity accountTo);

    Iterable<TransactionEntity> findAllByTransactionType(TransactionEntity.TransactionType transactionType);

    Iterable<TransactionEntity> findAllByTransactionTypeAndUser(TransactionEntity.TransactionType transactionType, UserEntity user);

}

