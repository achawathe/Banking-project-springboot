package com.achawathe.Banking.project.repositories;

import com.achawathe.Banking.project.TestDataUtil;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserEntityRepositoryIntegrationTests {

    private final UserRepository underTest;

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

    @Autowired
    public UserEntityRepositoryIntegrationTests(UserRepository underTest) {
        this.underTest = underTest;
    }


    @Test
    public void testThatUserCanBeCreatedAndRecalled(){
        UserEntity userEntity = TestDataUtil.createUserEntityA();
        UserEntity user = underTest.save(userEntity);

        Optional<UserEntity> result = underTest.findById(user.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    public void testThatManyUsersCanBeCreatedAndRecalled(){
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        UserEntity userEntityTestedA = underTest.save(userEntityA);
        UserEntity userEntityB = TestDataUtil.createUserEntityB();
        UserEntity userEntityTestedB =underTest.save(userEntityB);
        UserEntity UserEntityC = TestDataUtil.createUserEntityC();
        UserEntity userEntityTestedC = underTest.save(UserEntityC);

        Iterable<UserEntity> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactly(userEntityTestedA, userEntityTestedB, userEntityTestedC);

    }

    @Test
    public void testThatUserCanBeUpdatedAndRecalled(){
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        UserEntity userEntityTestedA = underTest.save(userEntityA);
        UserEntity userEntityTestedB = TestDataUtil.createUserEntityB();
        UserEntity userEntityTestedC = TestDataUtil.createUserEntityC();
        underTest.save(userEntityTestedA);
        underTest.save(userEntityTestedB);
        underTest.save(userEntityTestedC);
        Optional<UserEntity> result = underTest.findById(userEntityTestedA.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(userEntityTestedA, result.get());
    }

    @Test
    public void testThatUserCanBeDeleted(){
        UserEntity userEntityA = TestDataUtil.createUserEntityA();
        UserEntity userEntityTestedA = underTest.save(userEntityA);
        underTest.save(userEntityTestedA);
        Optional<UserEntity> result = underTest.findById(userEntityTestedA.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(userEntityTestedA, result.get());

        underTest.deleteById(userEntityTestedA.getId());
        result = underTest.findById(userEntityTestedA.getId());
        Assertions.assertFalse(result.isPresent());
    }


}
