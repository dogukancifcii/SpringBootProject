package com.dogukan.service.user;

import com.dogukan.payload.request.LoginRequest;
import com.dogukan.payload.response.authentication.AuthResponse;
import com.dogukan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {
        return null;
    }
}
