package com.achawathe.Banking.project.controllers;

import com.achawathe.Banking.project.TestDataUtil;
import com.achawathe.Banking.project.domain.dto.AccountDto;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.repositories.AccountRepository;
import com.achawathe.Banking.project.repositories.TransactionRepository;
import com.achawathe.Banking.project.repositories.UserRepository;
import com.achawathe.Banking.project.services.AccountService;
import com.achawathe.Banking.project.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AcccountControllerTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountService accountService;

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
    public AcccountControllerTests(MockMvc mockMvc, AccountService accountService) {
        this.mockMvc = mockMvc;
        this.accountService = accountService;
    }

    //creation tests
    @Test
    public void testThatCreateAccountSuccessfullyReturnsHttpStatusCreated() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntityA();
        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(userEntity);

        accountEntity.setUser(userEntity);

        AccountDto accountDto = modelMapper.map(accountEntity, AccountDto.class);

        String accountJson = objectMapper.writeValueAsString(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void testThatCreateAccountSuccessfullyReturnsHttpStatusBadRequestIfGivenBadData() throws Exception {

        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(null);



        String accountJson = objectMapper.writeValueAsString(accountEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testThatCreateAccountSuccessfullyReturnsAccountDto() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntityA();
        AccountEntity accountEntity = TestDataUtil.createAccountEntityA(userEntity);
        AccountDto accountDto = modelMapper.map(accountEntity, AccountDto.class);
        String accountJson = objectMapper.writeValueAsString(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // each account has a unique balance when created. Considering Ids can be unpredictable, verify that an account with the correct balance is created.
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(accountDto.getBalance()));
    }

    //Read tests
    @Test
    public void testThatGetAccountByIdSuccessfullyReturnsHttpStatusOk() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();

        UserEntity savedUser = userService.save(testUser);

        AccountEntity testAccount = TestDataUtil.createAccountEntityA(savedUser);

        AccountEntity savedAccount = accountService.save(testAccount); // Save account

        AccountDto accountDto = modelMapper.map(savedAccount, AccountDto.class);

        String accountJson = objectMapper.writeValueAsString(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + savedAccount.getAccountNumber().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testThatGetAccountByIdSuccessfullyReturnsAccountDto() throws Exception {

        UserEntity testUser = TestDataUtil.createUserEntityA();

        UserEntity savedUser = userService.save(testUser);

        AccountEntity testAccount = TestDataUtil.createAccountEntityA(savedUser);

        AccountEntity savedAccount = accountService.save(testAccount); // Save account

        AccountDto accountDto = modelMapper.map(savedAccount, AccountDto.class);

        String accountJson = objectMapper.writeValueAsString(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + savedAccount.getAccountNumber().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(savedAccount.getBalance().doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(savedAccount.getAccountNumber().toString()));

    }


    @Test
    public void testThatListAccountsSuccessfullyReturnsHttpStatusOk() throws Exception {

        UserEntity testUser = TestDataUtil.createUserEntityA();

        UserEntity savedUser = userService.save(testUser);

        AccountEntity testAccount = TestDataUtil.createAccountEntityA(savedUser);

        AccountEntity savedAccount = accountService.save(testAccount); // Save account

        AccountDto accountDto = modelMapper.map(savedAccount, AccountDto.class);

        String accountJson = objectMapper.writeValueAsString(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}