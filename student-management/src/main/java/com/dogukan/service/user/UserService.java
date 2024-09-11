package com.dogukan.service.user;

import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.UserResponse;
import com.dogukan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) {
        return null;
    }
}
