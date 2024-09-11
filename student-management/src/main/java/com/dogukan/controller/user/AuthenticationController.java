package com.dogukan.controller.user;

import com.dogukan.payload.messages.SuccessMessages;
import com.dogukan.payload.request.LoginRequest;
import com.dogukan.payload.request.UpdatePasswordRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.authentication.AuthResponse;
import com.dogukan.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    @PatchMapping("/updatePassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {

        //Todo:HttpServletRequest nedir arastir.
        authenticationService.updatePassword(updatePasswordRequest, request);


        String response = SuccessMessages.PASSWORD_CHANGE_RESPONSE_MESSAGE;

        return ResponseEntity.ok(response);
    }
}
