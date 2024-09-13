package com.dogukan.payload.mappers;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.payload.request.abstracts.BaseUserRequest;
import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //burada BaseUserRequest yazma sebebimiz teacherda gondersek studentta gondersek userda gondersek kabul edicek.Cunku parrent kismi BaseUserRequest kismi
    public User mapUserRequestToUser(BaseUserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .built_in(userRequest.getBuiltIn())
                .build();
    }

    public UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();

    }

}
