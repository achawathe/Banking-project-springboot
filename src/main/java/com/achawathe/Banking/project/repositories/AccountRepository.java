package com.achawathe.Banking.project.repositories;

import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.Optional;
import java.util.UUID;


public interface AccountRepository extends JpaRepository<AccountEntity,UUID>,
        PagingAndSortingRepository<AccountEntity, UUID> {
    Iterable<AccountEntity> findAllByUser(UserEntity userObj);

    void deleteByAccountNumber(UUID accountNumber);

    boolean existsAccountEntityByAccountNumberAndIsDeletedFalse(UUID accountNumber);


    Iterable<AccountEntity> findAllByUserAndIsDeletedFalse(UserEntity user);

    Optional<AccountEntity> findByAccountNumberAndIsDeletedFalse(UUID accountNumber);
}