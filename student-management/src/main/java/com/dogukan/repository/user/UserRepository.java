package com.dogukan.repository.user;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.payload.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    //asagida optional donsek daha iyi bir null kontrolu yapabiliriz.
    User findByUsernameEquals(String username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String phone);

    //: ile parametre gelecek demis olduk
    @Query("select u from User u where u.userRole.roleName = :roleName")
    Page<User> findByUserByRole(String roleName, Pageable pageable);
}
