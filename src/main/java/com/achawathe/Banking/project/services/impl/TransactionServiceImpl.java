package com.achawathe.Banking.project.services.impl;

import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.repositories.TransactionRepository;
import com.achawathe.Banking.project.services.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Override
    public List<TransactionEntity> findAllByUser(UserEntity user) {
        return StreamSupport.stream(transactionRepository.findAllByUser(user).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntity> findAllByAccountFromOrAccountTo(AccountEntity accountFrom, AccountEntity accountTo) {
        return StreamSupport.stream(transactionRepository.findAllByAccountFromOrAccountTo(accountFrom,accountTo).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntity> findAllByAccountFromAndAccountTo(AccountEntity accountFrom, AccountEntity accountTo) {
        return StreamSupport.stream(transactionRepository.findAllByAccountFromAndAccountTo(accountFrom,accountTo).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntity> findAllByTransactionType(TransactionEntity.TransactionType transactionType) {
        return StreamSupport.stream(transactionRepository.findAllByTransactionType(transactionType).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntity> findAllByTransactionTypeAndUser(TransactionEntity.TransactionType transactionType, UserEntity user) {
        return StreamSupport.stream(transactionRepository.findAllByTransactionTypeAndUser(transactionType,user).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntity> findAll() {
        return StreamSupport.stream(transactionRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public TransactionEntity save(TransactionEntity transactionEntity) {
        return transactionRepository.save(transactionEntity);
    }

    @Override
    public void delete(TransactionEntity transactionEntity) {
        transactionRepository.delete(transactionEntity);
    }

    @Override
    public TransactionEntity update(TransactionEntity transactionEntity) {
        return transactionRepository.save(transactionEntity);
    }

    @Override
    public boolean existsById(String id) {
        return transactionRepository.existsById(id);
    }

    @Override
    public boolean validTransaction(TransactionEntity transactionEntity) {
        if(transactionEntity.getTransactionType().equals(TransactionEntity.TransactionType.OPENING)) {
            return  transactionEntity.getAmount().doubleValue() >= 0;
        } else if (transactionEntity.getTransactionType().equals(TransactionEntity.TransactionType.DEPOSIT)) {
            return !(transactionEntity.getAccountFrom() == null) || !(transactionEntity.getAccountTo() == null) && transactionEntity.getAmount().doubleValue() >= 0 && !(transactionEntity.getUser() == null) && (transactionEntity.getAccountTo().getUser().equals(transactionEntity.getUser()) || transactionEntity.getAccountFrom().getUser().equals(transactionEntity.getUser())) ; //make sure at least one is filled in
        } else if (transactionEntity.getTransactionType().equals(TransactionEntity.TransactionType.CLOSING)) {
            return !(transactionEntity.getAccountFrom() == null) || !(transactionEntity.getAccountTo() == null) && !(transactionEntity.getUser() == null) && (transactionEntity.getAccountTo().getUser().equals(transactionEntity.getUser()) || transactionEntity.getAccountFrom().getUser().equals(transactionEntity.getUser())); //make sure at least one is filled in
        }else if (transactionEntity.getTransactionType().equals(TransactionEntity.TransactionType.WITHDRAWAL)) {
            return  transactionEntity.getAmount().doubleValue() >= 0 && (transactionEntity.getAccountFrom() != null || transactionEntity.getAccountTo() != null) && (Objects.requireNonNull(transactionEntity.getAccountTo()).getBalance().doubleValue() >= transactionEntity.getAmount().doubleValue() || Objects.requireNonNull(transactionEntity.getAccountFrom()).getBalance().doubleValue() >= transactionEntity.getAmount().doubleValue()) && !(transactionEntity.getUser() == null) && (transactionEntity.getAccountTo().getUser().equals(transactionEntity.getUser()) || transactionEntity.getAccountFrom().getUser().equals(transactionEntity.getUser()));
        }

        return  transactionEntity.getAmount().doubleValue() >= 0 && transactionEntity.getAccountFrom() != null && transactionEntity.getAccountTo() != null && Objects.requireNonNull(transactionEntity.getAccountFrom()).getBalance().doubleValue() >= transactionEntity.getAmount().doubleValue() && transactionEntity.getTransactionType().equals(TransactionEntity.TransactionType.TRANSFER) && !(transactionEntity.getUser() == null) && transactionEntity.getAccountTo().getUser().equals(transactionEntity.getUser()) && transactionEntity.getAccountFrom().getUser().equals(transactionEntity.getUser()) && ! transactionEntity.getAccountTo().equals(transactionEntity.getAccountFrom());
    }

    @Override
    public Optional<TransactionEntity> getById(String id) {
        return transactionRepository.findById(id);
    }
}
