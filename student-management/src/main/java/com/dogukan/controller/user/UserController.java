package com.dogukan.controller.user;

import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.user.UserResponse;
import com.dogukan.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save/{userRole}") //http://localhost:8080/user/save/Admin + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN')") //sadece admin yapsin
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(
            @RequestBody @Valid UserRequest userRequest,
            @PathVariable String userRole) {


        return ResponseEntity.ok(userService.saveUser(userRequest, userRole));
    }

}
