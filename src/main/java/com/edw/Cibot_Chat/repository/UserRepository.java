package com.edw.Cibot_Chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.enums.Rol;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByRolIn(List<Rol> roles);
    List<User> findByRol(Rol rol);

    Optional<User> findByEmail(String email);
}
