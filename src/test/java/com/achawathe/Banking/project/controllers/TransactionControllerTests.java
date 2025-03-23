package com.achawathe.Banking.project.controllers;

import com.achawathe.Banking.project.TestDataUtil;
import com.achawathe.Banking.project.domain.dto.TransactionDto;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.repositories.AccountRepository;
import com.achawathe.Banking.project.repositories.TransactionRepository;
import com.achawathe.Banking.project.repositories.UserRepository;
import com.achawathe.Banking.project.services.AccountService;
import com.achawathe.Banking.project.services.TransactionService;
import com.achawathe.Banking.project.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransactionControllerTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

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
    public TransactionControllerTests(MockMvc mockMvc, TransactionService transactionService) {
        this.mockMvc = mockMvc;
        this.transactionService = transactionService;
    }


    @Test
    public void testThatTransferBetweenTwoAccountsWithValidAccountsSuccessfullyLogsTransaction() throws Exception {

        UserEntity userEntity = TestDataUtil.createUserEntityA();
        UserEntity savedUserEntity = userRepository.save(userEntity);

        AccountEntity accountEntity1 = TestDataUtil.createAccountEntityA(savedUserEntity);
        AccountEntity accountEntity2 = TestDataUtil.createAccountEntityB(savedUserEntity);

        AccountEntity savedAccount1 =  accountRepository.save(accountEntity1);
        AccountEntity savedAccount2 =  accountRepository.save(accountEntity2);

        TransactionEntity transaction = TestDataUtil.createTransactionEntityForDifferentAccount(savedAccount1,savedAccount2, BigDecimal.valueOf(20), TransactionEntity.TransactionType.TRANSFER);




        mockMvc.perform(MockMvcRequestBuilders.put("/transaction/transfer/" + savedAccount1.getAccountNumber() + "_to_" + savedAccount2.getAccountNumber())
                        .content(String.valueOf(20))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountFrom.accountNumber").value(savedAccount1.getAccountNumber().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountTo.accountNumber").value(savedAccount2.getAccountNumber().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(transaction.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value(TransactionEntity.TransactionType.TRANSFER.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(savedUserEntity.getId()));
    }

    @Test
    public void testThatTransferBetweenTwoAccountsWithInvalidAccountsSuccessfullyLogsTransaction() throws Exception {

        UserEntity userEntity = TestDataUtil.createUserEntityA();
        UserEntity savedUserEntity = userRepository.save(userEntity);

        AccountEntity accountEntity1 = TestDataUtil.createAccountEntityA(savedUserEntity);

        AccountEntity savedAccount1 =  accountRepository.save(accountEntity1);

        mockMvc.perform(MockMvcRequestBuilders.put("/transaction/transfer/" + savedAccount1.getAccountNumber() + "_to_" + savedAccount1.getAccountNumber())
                        .content(String.valueOf(20))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}
