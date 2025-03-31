package com.achawathe.Banking.project.repositories;

import com.achawathe.Banking.project.TestDataUtil;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AccountEntityRepositoryIntegrationTests {

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
    public void testThatAccountCanSuccessfullyBeCreatedAndRecalled() {
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        userRepository.save(userEntityA);
        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(userEntityA);
        AccountEntity savedAccount = accountRepository.save(accountEntity);

        assertThat(accountEntity).isNotNull();
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUser()).isEqualTo(accountEntity.getUser());
        assertThat(savedAccount.getBalance()).isEqualTo(accountEntity.getBalance());
    }


    @Test
    public void testThatManyAccountsCanSuccessfullyBeCreatedAndRecalled() {
        // Ensure users are fully persisted before assigning them to accounts
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        UserEntity savedUserEntityA = userRepository.save(userEntityA);

        UserEntity userEntityB = TestDataUtil.createUserEntityB();
        UserEntity savedUserEntityB = userRepository.save(userEntityB);

        // Now create accounts referencing the persisted users
        AccountEntity accountEntityA = TestDataUtil.createAccountEntityA(savedUserEntityA);
        AccountEntity savedAccountA = accountRepository.save(accountEntityA);

        AccountEntity accountEntityB = TestDataUtil.createAccountEntityB(savedUserEntityB);
        AccountEntity savedAccountB = accountRepository.save(accountEntityB);

        // Assertions
        assertThat(savedAccountA).isNotNull();
        assertThat(savedAccountB).isNotNull();
        assertThat(savedAccountA.getBalance()).isEqualTo(accountEntityA.getBalance());
        assertThat(savedAccountB.getBalance()).isEqualTo(accountEntityB.getBalance());
        assertThat(savedAccountA.getUser()).isEqualTo(savedUserEntityA);
        assertThat(savedAccountB.getUser()).isEqualTo(savedUserEntityB);
    }





    @Test
    public void testThatAccountCanSuccessfullyBeUpdated() {
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        userRepository.save(userEntityA);
        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(userEntityA);
        AccountEntity savedAccount = accountRepository.save(accountEntity);


        assertThat(accountEntity).isNotNull();
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUser()).isEqualTo(accountEntity.getUser());
        assertThat(savedAccount.getBalance()).isEqualTo(accountEntity.getBalance());

        accountEntity.setBalance(BigDecimal.valueOf(15));
        accountRepository.save(accountEntity);

        assertThat(accountEntity).isNotNull();
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUser()).isEqualTo(accountEntity.getUser());
        assertThat(savedAccount.getBalance()).isEqualTo(accountEntity.getBalance());

    }

    @Test
    public void testThatAccountCanSuccessfullyBeDeleted() {
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        userRepository.save(userEntityA);
        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(userEntityA);
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        assertThat(accountEntity).isNotNull();
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUser()).isEqualTo(accountEntity.getUser());
        assertThat(savedAccount.getBalance()).isEqualTo(accountEntity.getBalance());
        accountRepository.delete(accountEntity);
        assertThat(accountRepository.existsAccountEntityByAccountNumberAndIsDeletedFalse(savedAccount.getAccountNumber())).isFalse();
    }




}
