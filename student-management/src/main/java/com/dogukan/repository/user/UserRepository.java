package com.dogukan.repository.user;

import com.dogukan.entity.concretes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //asagida optional donsek daha iyi bir null kontrolu yapabiliriz.
    User findByUsernameEquals(String username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String phone);
}
