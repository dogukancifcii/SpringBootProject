package com.dogukan.service.user;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.payload.mappers.UserMapper;
import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.UserResponse;
import com.dogukan.repository.user.UserRepository;
import com.dogukan.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;

    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) {
        //girilen username - ssn - phoneNumber-email unique mi kontrolü
        uniquePropertyValidator.checkDuplicate(
                userRequest.getUsername(),
                userRequest.getSsn(),
                userRequest.getPhoneNumber(),
                userRequest.getEmail()
        );

        //DTO --> POJO
        User user = userMapper.mapUserRequestToUser(userRequest);
        //!!! Rol bilgisini setleniyor
        if (userRole.equalsIgnoreCase(RoleType.ADMIN.name())) {
            if (Objects.equals(userRequest.getUsername(), "Admin")) {
                user.setBuilt_in(true);
            }
            user.setUserRole();
        }

    }
}
