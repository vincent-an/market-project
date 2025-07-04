package likeLion2025.Left.domain.user.controller;

import likeLion2025.Left.domain.user.dto.SignupRequest;
import likeLion2025.Left.domain.user.dto.UserResponse;
import likeLion2025.Left.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j //로그 출력 에너테이션
@RestController
@RequestMapping("/eushop")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    //spring boot jwt 로그아웃 후 로그인 안됨
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> Signup(@RequestBody SignupRequest request){
        log.info("회원가입 시작: 이메일={}, 닉네임={}", request.getEmail(), request.getNickname());
        UserResponse response = userService.joinProcess(request);
        return ResponseEntity.ok(response);
    }
}
