package com.example.demo.user.controller;

import com.example.demo.user.controller.port.AuthenticationService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "유저(users)")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Builder
public class UserController {

    private final UserReadService userReadService;
    private final AuthenticationService authenticationService;
    private final UserUpdateService userUpdateService;


    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(UserResponse.toResponse(userReadService.getById(id)));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        authenticationService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponse> getMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
    ) {
        User user = userReadService.getByEmail(email);
        authenticationService.login(user.getId());
        return ResponseEntity
            .ok()
            .body(toMyProfileResponse(user));
    }

    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<MyProfileResponse> updateMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
        @RequestBody UserUpdate userUpdate
    ) {
        User user = userReadService.getByEmail(email);
        user = userUpdateService.update(user.getId(), userUpdate);
        return ResponseEntity
            .ok()
            .body(toMyProfileResponse(user));
    }



    public MyProfileResponse toMyProfileResponse(User user) {
        MyProfileResponse myProfileResponse = new MyProfileResponse();
        myProfileResponse.setId(user.getId());
        myProfileResponse.setEmail(user.getEmail());
        myProfileResponse.setNickname(user.getNickname());
        myProfileResponse.setStatus(user.getStatus());
        myProfileResponse.setAddress(user.getAddress());
        myProfileResponse.setLastLoginAt(user.getLastLoginAt());
        return myProfileResponse;
    }
}