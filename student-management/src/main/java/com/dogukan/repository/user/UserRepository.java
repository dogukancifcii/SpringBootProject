package com.dogukan.repository.user;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.payload.response.user.StudentResponse;
import com.dogukan.payload.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

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

    User findByUsername(String userName);

    List<User> getUserByNameContaining(String name);

    @Query("select count(u) from User u where u.userRole.roleType= ?1")
    long countAdmin(RoleType roleType);

    List<User> findByAdvisorTeacherId(Long id);

    @Query("SELECT u from User u where u.isAdvisor =?1")
    List<User> findAllByAdvisor(Boolean aTrue);

    @Query("select max (u.studentNumber) from User u")
    int getMaxStudentNumber();

    @Query(value = "select (count (u)>0) from User u where u.userRole.roleType = ?1")
    boolean findStudent(RoleType roleType);

    @Query("select u from User where u.id in :studentIds")
        //buradaki in liste verdigimiz icin kullanilan in oluyor.
    List<User> findByIdsEquals(Long[] studentIds);
}
