package com.dogukan.controller.user;

import com.dogukan.payload.request.user.UserRequest;
import com.dogukan.payload.response.ResponseMessage;
import com.dogukan.payload.response.abstracts.BaseUserResponse;
import com.dogukan.payload.response.user.UserResponse;
import com.dogukan.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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


    @GetMapping("/getAllUserByPage/{userRole}")//http://localhost:8080/user/getAllUserByPage/Admin + GET
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getUserByPage(
            @PathVariable String userRole,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        Page<UserResponse> adminsOrDeans = userService.getUsersByPage(page, size, sort, type, userRole);
        return new ResponseEntity<>(adminsOrDeans, HttpStatus.OK);
    }

    // hoca / unutmus
    @GetMapping("/getUserById/{userId}") //http://localhost:8080/user/getUserById/1 + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<BaseUserResponse> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }
}
