package com.example.obs.dataAccess;

import com.example.obs.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findByIdentityNumber(String identityNumber);
    
    boolean existsByEmail(String email);

}