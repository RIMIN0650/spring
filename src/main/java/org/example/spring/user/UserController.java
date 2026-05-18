package org.example.spring.user;

import lombok.RequiredArgsConstructor;
import org.example.spring.user.model.AuthUserDetails;
import org.example.spring.user.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDto.SignupReq dto) {
        userService.signup(dto);
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto.userInfoRes> getLoggedInUserInfo(@AuthenticationPrincipal AuthUserDetails authUserDetails) {

        UserDto.userInfoRes dto = UserDto.userInfoRes.builder()
                .idx(authUserDetails.getIdx())
                .name(authUserDetails.getUsername())
                .role(authUserDetails.getRole())
                .billingKey(authUserDetails.getBillingKey())
                .build();

        return ResponseEntity.ok(dto);
    }

}
