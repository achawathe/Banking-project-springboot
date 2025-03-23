package com.achawathe.Banking.project.services;

import com.achawathe.Banking.project.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {
    UserEntity save(UserEntity user);
    void deleteUser(UserEntity user);
    UserEntity updateUser(Long id, UserEntity user);
    List<UserEntity> getUsers();
    Optional<UserEntity> getUser(String Name);
    Optional<UserEntity> getUser(Long id);
    boolean userExists(Long id);
    boolean userExists(String Name);
}
