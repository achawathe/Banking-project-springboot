package com.achawathe.Banking.project.repositories;

import com.achawathe.Banking.project.domain.entities.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> , PagingAndSortingRepository<UserEntity,Long> {
    @Override
    @NotNull
    Optional<UserEntity> findById(@NotNull Long id);
    Optional<UserEntity> findByName(String name);
    boolean existsByName(String name);

}
