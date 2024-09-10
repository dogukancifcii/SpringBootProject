package com.dogukan.controller.user;

import com.dogukan.payload.request.LoginRequest;
import com.dogukan.payload.response.authentication.AuthResponse;
import com.dogukan.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //post olma mantigi body icinde bilgi gönderdiğimiz için.Bu güvenlik sağlar. Get işlemi ile yapılmaz. Hassas bilgiler post ile yapılır.
    @PostMapping("/login") //http://localhost:8080/auth/login + POST
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

}
