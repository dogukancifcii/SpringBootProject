package com.dogukan.service.user;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.entity.enums.RoleType;
import com.dogukan.exception.ResourceNotFoundException;
import com.dogukan.payload.mappers.UserMapper;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.abstracts.BaseUserResponse;
import com.dogukan.payload.response.user.UserResponse;
import com.dogukan.repository.user.UserRepository;
import com.dogukan.service.helper.PageableHelper;
import com.dogukan.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final PageableHelper pageableHelper;

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
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        } else if (userRole.equalsIgnoreCase("Dean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        } else {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_ROLE_MESSAGE, userRole));
        }
        //password encode ediliyor
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //advisor degeri setleniyor(Admin-Manager ve AsstManagerlarin advisro olma ihtimali yok)
        user.setIsAdvisor(Boolean.FALSE);

        User savedUser = userRepository.save(user);
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }

    public Page<UserResponse> getUsersByPage(int page, int size, String sort, String type, String userRole) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        return userRepository.findByUserByRole(userRole, pageable).map(userMapper::mapUserToUserResponse);
    }

    public ResponseMessage<BaseUserResponse> getUserById(Long userId) {

        BaseUserResponse baseUserResponse;

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));

        // UserResponse --> Admin , Manager, Assistant_Manager
        // TeacherResponse --> Teacher
        // StudentResponse --> Student
        if (user.getUserRole().getRoleType() == RoleType.STUDENT) {
            baseUserResponse = userMapper.mapUserToStudentResponse(user);
        } else if (user.getUserRole().getRoleType() == RoleType.TEACHER) {
            baseUserResponse = userMapper.mapUserToTeacherResponse(user);
        } else {
            baseUserResponse = userMapper.mapUserToUserResponse(user);
        }

        return ResponseMessage.<BaseUserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(baseUserResponse)
                .build();
    }
}
