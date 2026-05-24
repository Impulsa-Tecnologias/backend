package com.edw.Cibot_Chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edw.Cibot_Chat.entity.Rol;
import com.edw.Cibot_Chat.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByRolIn(List<Rol> roles);
    List<User> findByRol(Rol rol);
}
