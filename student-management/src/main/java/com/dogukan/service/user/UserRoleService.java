package com.dogukan.service.user;


import com.dogukan.entity.concretes.user.UserRole;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRole(RoleType roleType) {
        return userRoleRepository.findByEnumRoleEquals(roleType).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND));
    }

    public List<UserRole> getAllUserRole() {
        return userRoleRepository.findAll();
    }

}
