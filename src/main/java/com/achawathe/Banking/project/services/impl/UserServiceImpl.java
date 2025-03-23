package com.achawathe.Banking.project.services.impl;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.repositories.UserRepository;
import com.achawathe.Banking.project.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserEntity updateUser(Long id, UserEntity user) throws ConfigDataResourceNotFoundException {
       user.setId(id);

       return userRepository.findById(id).map(existingUser -> {
           Optional.ofNullable(user.getName()).ifPresent(existingUser::setName);
       return userRepository.save(existingUser);}).orElseThrow();
    }

    @Override
    public List<UserEntity> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> getUser(String Name) {
        return userRepository.findByName(Name);
    }

    @Override
    public Optional<UserEntity> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean userExists (Long id){
        return userRepository.findById(id).isPresent();
    }

    @Override
    public boolean userExists(String Name) {
        return userRepository.existsByName(Name);
    }

}
