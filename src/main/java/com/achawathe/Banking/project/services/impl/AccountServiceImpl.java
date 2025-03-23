package com.achawathe.Banking.project.services.impl;

import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.repositories.AccountRepository;
import com.achawathe.Banking.project.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor // Lombok automatically creates a constructor for final fields
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<AccountEntity> findByAccountNumber(UUID accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public AccountEntity update(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public void delete(UUID accountNumber) {
        accountRepository.deleteByAccountNumber(accountNumber);
    }

    @Override
    public List<AccountEntity> getAllAccounts() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<AccountEntity> getAccountByUser(UserEntity user) {
        return StreamSupport.stream(accountRepository.findAllByUser(user).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public boolean accountExists(UUID accountNumber) {
        return accountRepository.existsAccountEntityByAccountNumber(accountNumber);
    }


    @Override
    public boolean validateAccount(AccountEntity account) {
        return isAccountValid(account);
    }

    // Private helper method for validation
    private boolean isAccountValid(AccountEntity account) {
        return account != null &&
                account.getUser() != null &&
                account.getBalance() != null &&
                account.getBalance().compareTo(BigDecimal.ZERO) >= 0;
    }
}
