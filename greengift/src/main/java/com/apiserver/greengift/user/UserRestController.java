package com.apiserver.greengift.user;

import com.apiserver.greengift._core.security.CustomUserDetails;
import com.apiserver.greengift._core.security.JWTProvider;
import com.apiserver.greengift._core.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequest.SignUpDTO requestDTO) {
        userService.signup(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO) {
        String token = userService.login(requestDTO);
        return ResponseEntity
                .ok()
                .header(JWTProvider.HEADER, token)
                .body(ApiUtils.success(null));
    }

    @GetMapping("/grade")
    public ResponseEntity<?> getUserGrade(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse.FindGrade responseDTO = userService.getUserGrade(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

}
