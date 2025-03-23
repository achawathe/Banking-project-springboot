package com.achawathe.Banking.project.services;

import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//Creating objects and managing objects
public interface AccountService {
    AccountEntity save(AccountEntity account);
    Optional<AccountEntity> findByAccountNumber(UUID accountNumber);
    AccountEntity update(AccountEntity account);
    void delete(UUID AccountNumber);
    List<AccountEntity> getAllAccounts();
    List<AccountEntity> getAccountByUser(UserEntity user);
    boolean accountExists(UUID AccountNumber);
    boolean validateAccount(AccountEntity account);
}
