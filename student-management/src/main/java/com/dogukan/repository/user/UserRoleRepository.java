package com.dogukan.repository.user;

import com.dogukan.entity.concretes.user.UserRole;
import com.dogukan.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    //buradaki ?1 kismi 1. parametre demek oluyor.
    //JPQL yazmis olduk
    @Query("select r from UserRole r where r.roleType = ?1")
    Optional<UserRole> findByEnumRoleEquals(RoleType roleType);
}
