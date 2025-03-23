package com.achawathe.Banking.project.controllers;

import com.achawathe.Banking.project.domain.dto.UserDto;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.mappers.impl.UserMapperImpl;
import com.achawathe.Banking.project.repositories.AccountRepository;
import com.achawathe.Banking.project.repositories.TransactionRepository;
import com.achawathe.Banking.project.repositories.UserRepository;
import com.achawathe.Banking.project.services.AccountService;
import com.achawathe.Banking.project.services.TransactionService;
import com.achawathe.Banking.project.services.UserService;
import com.achawathe.Banking.project.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTests {


    private UserService userService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapperImpl userMapper;

    @Mock
    private AccountService accountService;


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
    public UserControllerTests(MockMvc mockMvc, UserService userService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    //create user tests


    @Test
    public void testThatCreateUsersSuccessfullyIsOk() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserDto userDtoUpdate = modelMapper.map(testUser, UserDto.class);
        String userDtoJson = objectMapper.writeValueAsString(userDtoUpdate);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/create-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userDtoJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateUsersSuccessfullyReturnsUser() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserDto userDtoUpdate = modelMapper.map(testUser, UserDto.class);
        String userDtoJson = objectMapper.writeValueAsString(userDtoUpdate);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))// Print response
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Arthur"));// First, check if status is 201

    }

    @Test
    public void testThatCreateUserWithNullBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")  // FIXED NULL JSON HANDLING
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testThatCreateUserWithEmptyNameReturnsBadRequest() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        testUser.setId(null);
        testUser.setName(null);
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    //Read User Tests
    @Test
    public void testThatReadUserSuccessfullyReturnsHttpStatusOk() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        userService.save(testUser);
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatReadUserSuccessfullyReturnsUser() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        userService.save(testUser);
        String userJson = objectMapper.writeValueAsString(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Arthur"));
    }

    @Test
    public void testThatListUsersSuccessfullyReturnsHttpStatusOk() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserEntity testUser2 = TestDataUtil.createUserEntityB();
        userService.save(testUser);
        userService.save(testUser2);
        String userJson = objectMapper.writeValueAsString(testUser);
        String userJson2 = objectMapper.writeValueAsString(testUser2);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListUsersSuccessfullyReturnsUserList() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserEntity testUser2 = TestDataUtil.createUserEntityB();
        userService.save(testUser);
        userService.save(testUser2);
        String userJson = objectMapper.writeValueAsString(testUser);
        String userJson2 = objectMapper.writeValueAsString(testUser2);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Arthur"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jeremy"));
    }

    //Update User Tests
    @Test
    public void testUpdateUserSuccessfullyReturnsHttpStatusOk() throws Exception {
        // Save a test user
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserEntity savedUser = userService.save(testUser);

        // Create DTO for update
        UserDto userDtoUpdate = new UserDto();
        userDtoUpdate.setName("Updated Name");
        userDtoUpdate.setId(savedUser.getId());

        String userDtoJson = objectMapper.writeValueAsString(userDtoUpdate);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/user/" + savedUser.getId())  // FIXED ENDPOINT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateUserSuccessfullyReturnsUser() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserEntity savedUser = userService.save(testUser);
        // Create DTO for update
        UserDto userDtoUpdate = new UserDto();
        userDtoUpdate.setName("Updated Name");
        userDtoUpdate.setId(savedUser.getId());
        String userDtoJson = objectMapper.writeValueAsString(userDtoUpdate);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/user/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testUpdateUserWithNullBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/user/" + TestDataUtil.createUserEntityA().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Delete Users Tests
    @Test
    public void testDeleteUserSuccessfullyReturnsHttpStatusOk() throws Exception {
        UserEntity testUser = TestDataUtil.createUserEntityA();
        UserEntity savedUser = userService.save(testUser);
        String userJson = objectMapper.writeValueAsString(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/user/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()); //return of controller
    }



    @Test
    public void testDeleteUserWithNullBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/user/" + TestDataUtil.createUserEntityA().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



}
