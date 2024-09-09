package com.dogukan.repository;

import com.dogukan.entity.concretes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //asagida optional donsek daha iyi bir null kontrolu yapabiliriz.
    User findByUsernameEquals(String username);
}
