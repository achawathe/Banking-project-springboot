package com.achawathe.Banking.project.controllers;

import com.achawathe.Banking.project.domain.dto.UserDto;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.mappers.Mapper;
import com.achawathe.Banking.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// Endpoints
@RestController
public class UserController {

    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;

    UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    //list users
    @GetMapping("/users")
    public List<UserDto> getAllUsers(){
        List<UserEntity> users = userService.getUsers();
        //Edge case safety not important; front end would handle this issue
        return users.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    //get user by id
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        if (userService.userExists(id)) {

            Optional<UserEntity> userEntity = userService.getUser(id);

            //verify valid id
            if (userEntity.isEmpty()) {
                return new ResponseEntity<>("User is empty",HttpStatus.NOT_FOUND);
            } else if (userEntity.get().getId() == null) {
                return new ResponseEntity<>("User ID is Null",HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(userMapper.mapTo(userEntity.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>("User does not exist in database", HttpStatus.NOT_FOUND);
    }

    //delete a user
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<UserEntity> userEntity = userService.getUser(id);
        if (userEntity.isEmpty() || userEntity.get().getId() == null) {
            return new ResponseEntity<>("User found, but is either empty, or has no ID.",HttpStatus.NOT_FOUND);
        }

        UserEntity user = userEntity.get();
        if (userService.userExists(user.getId())) {
            userService.deleteUser(user);
            return new ResponseEntity<>("User has been successfully deleted", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>("User Does Not Exist",HttpStatus.NOT_FOUND);
    }

    //partial or full update user
    @PatchMapping( value = "/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        //verify valid user
        if(userService.userExists(id)) {

            UserEntity updatedUser = userService.updateUser(id, userEntity);

            return new ResponseEntity<>(userMapper.mapTo(updatedUser), HttpStatus.OK);
        }
        return new ResponseEntity<>("User Does Not Exist",HttpStatus.NOT_FOUND);
    }


    //create user
    @PostMapping(value="/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);

        if(userEntity == null || userEntity.getName() == null) {
            return new ResponseEntity<>("User Entity or Response Entity is Null",HttpStatus.BAD_REQUEST);
        }
        //check if user created is valid, User can have generated ID, so we just need to make sure that the id isn't null.
        if(userEntity.getId() !=null && userService.userExists(userEntity.getId())) {
            return new ResponseEntity<>("User exists with same ID",HttpStatus.CONFLICT);
        }
        //assume unique names
        if(userService.userExists(userEntity.getName())) {
            return new ResponseEntity<>("User Already Exists with the same Name",HttpStatus.CONFLICT);
        }


        UserEntity savedUser = userService.save(userEntity);


        return new ResponseEntity<>(userMapper.mapTo(savedUser), HttpStatus.CREATED);
    }


}
