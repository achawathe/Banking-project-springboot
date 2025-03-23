package com.achawathe.Banking.project.repositories;

import com.achawathe.Banking.project.TestDataUtil;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransactionEntityRepositoryIntegrationTests {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }


    @AfterEach
    void cleanUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void testThatTransactionCanSuccessfullyBeCreatedAndRecalled() {
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        userRepository.save(userEntityA);
        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(userEntityA);
        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
        assertThat(accountEntity).isNotNull();
        assertThat(savedAccountEntity).isNotNull();
        assertThat(savedAccountEntity.getUser()).isEqualTo(accountEntity.getUser());
        assertThat(savedAccountEntity.getBalance()).isEqualTo(accountEntity.getBalance());

        TransactionEntity transaction = TestDataUtil.createTransactionEntityforSameAccount(savedAccountEntity, BigDecimal.valueOf(30), TransactionEntity.TransactionType.OPENING);

        TransactionEntity savedTransaction =  transactionRepository.save(transaction);



        assertThat(transaction).isNotNull();
        assertThat(TransactionEntity.TransactionType.OPENING).isEqualTo(transaction.getTransactionType());
        assertThat(transaction).isEqualTo(savedTransaction);

    }

    @Test
    public void testThatMultipleTransactionsCanBeCreatedAndRecalled() {
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        userRepository.save(userEntityA);
        AccountEntity accountEntityA = TestDataUtil.createAccountEntityA(userEntityA);
        AccountEntity savedAccountEntity = accountRepository.save(accountEntityA);
        AccountEntity accountEntityB = TestDataUtil.createAccountEntityB(userEntityA);

        AccountEntity savedAccountEntity2 = accountRepository.save(accountEntityB);
        assertThat(accountEntityA).isNotNull();
        assertThat(savedAccountEntity).isNotNull();
        assertThat(savedAccountEntity.getUser()).isEqualTo(accountEntityA.getUser());
        assertThat(savedAccountEntity.getBalance()).isEqualTo(accountEntityA.getBalance());
        assertThat(savedAccountEntity2).isNotNull();
        assertThat(savedAccountEntity2.getUser()).isEqualTo(accountEntityB.getUser());
        assertThat(savedAccountEntity2.getBalance()).isEqualTo(accountEntityB.getBalance());

        TransactionEntity transactionA = TestDataUtil.createTransactionEntityForDifferentAccount(accountEntityA,accountEntityB, BigDecimal.valueOf(30), TransactionEntity.TransactionType.TRANSFER);
        TransactionEntity transactionB = TestDataUtil.createTransactionEntityforSameAccount(accountEntityA, BigDecimal.valueOf(30), TransactionEntity.TransactionType.DEPOSIT);

        TransactionEntity savedTransactionA = transactionRepository.save(transactionA);
        TransactionEntity savedTransactionB = transactionRepository.save(transactionB);

        assertThat(transactionA).isNotNull();
        assertThat(savedTransactionA).isNotNull();
        assertThat(savedTransactionA.getUser()).isEqualTo(accountEntityA.getUser());
        assertThat(transactionA.getTransactionType()).isEqualTo(TransactionEntity.TransactionType.TRANSFER);
        assertThat(transactionA).isEqualTo(savedTransactionA);
        assertThat(transactionB).isNotNull();
        assertThat(TransactionEntity.TransactionType.DEPOSIT).isEqualTo(transactionB.getTransactionType());
        assertThat(transactionB).isEqualTo(savedTransactionB);

        List<TransactionEntity> transactions = transactionRepository.findAll();

        assertThat(transactions).isNotNull();
        assertThat(transactions.size()).isEqualTo(2);
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(TransactionEntity.TransactionType.TRANSFER);
        assertThat(transactions.get(1).getTransactionType()).isEqualTo(TransactionEntity.TransactionType.DEPOSIT);
        assertThat(transactions.get(0).getAccountFrom().getBalance().longValue()).isEqualTo(savedTransactionA.getAccountFrom().getBalance().longValue());
        assertThat(transactions.get(1).getAccountFrom().getBalance().longValue()).isEqualTo(savedTransactionB.getAccountFrom().getBalance().longValue());
        assertThat(transactions.get(0).getAccountTo().getBalance().longValue()).isEqualTo(savedTransactionA.getAccountTo().getBalance().longValue());
        assertThat(transactions.get(1).getAccountTo().getBalance().longValue()).isEqualTo(savedTransactionB.getAccountTo().getBalance().longValue());
        assertThat(transactions.get(0).getAccountFrom().getUser()).isEqualTo(savedTransactionA.getUser());
        assertThat(transactions.get(1).getAccountTo().getUser()).isEqualTo(savedTransactionB.getUser());
        assertThat(transactions.get(0).getAccountFrom().getUser()).isEqualTo(savedTransactionA.getAccountFrom().getUser());
        assertThat(transactions.get(1).getAccountFrom().getUser()).isEqualTo(savedTransactionB.getAccountFrom().getUser());
        assertThat(transactions.get(0).getAccountTo().getAccountNumber()).isEqualTo(savedTransactionA.getAccountTo().getAccountNumber());
        assertThat(transactions.get(1).getAccountFrom().getAccountNumber()).isEqualTo(savedTransactionB.getAccountFrom().getAccountNumber());
        assertThat(transactions.get(0).getId()).isEqualTo(savedTransactionA.getId());
        assertThat(transactions.get(1).getId()).isEqualTo(savedTransactionB.getId());

    }


}
