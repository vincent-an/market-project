package likeLion2025.Left.domain.user.controller;

import likeLion2025.Left.domain.post.dto.response.PostMainIntroResponse;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.user.dto.MyPostsDTO;
import likeLion2025.Left.domain.user.dto.request.SignupRequest;
import likeLion2025.Left.domain.user.dto.UserProfileDTO;
import likeLion2025.Left.domain.user.dto.request.UserUpdateRequest;
import likeLion2025.Left.domain.user.dto.response.UserResponse;
import likeLion2025.Left.domain.user.dto.response.UserUpdateResponse;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import likeLion2025.Left.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j //로그 출력 에너테이션
@RestController
@RequestMapping("/eushop")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //회원가입
    //spring boot jwt 로그아웃 후 로그인 안됨
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> Signup(@RequestBody SignupRequest request){
        log.info("회원가입 시작: 이메일={}, 닉네임={}", request.getEmail(), request.getNickname());
        UserResponse response = userService.joinProcess(request);
        return ResponseEntity.ok(response);
    }

    //프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileDTO profile = userService.getUserProfile(email);
        return ResponseEntity.ok(profile);
    }

    //개인정보 수정
    @PatchMapping("/profile")
    public ResponseEntity<UserUpdateResponse> updateProfile(@RequestBody UserUpdateRequest request,
                                                            Authentication authentication) {
        String email = authentication.getName();
        log.info("개인정보 수정 시작 이메일: {}", email);
        UserUpdateResponse response = userService.updateProfile(email, request);
        return ResponseEntity.ok(response);
    }

    //내가 등록한 상품
    @GetMapping("/prorile/myposts/{postType}")
    public ResponseEntity<List<MyPostsDTO>> getMyPostsByType(@PathVariable("postType") String postType,
            Authentication authentication) {

        String email = authentication.getName();
        PostType type;
        try {
            type = PostType.valueOf(postType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<PostStatus> statuses;
        if (type == PostType.SELL) {
            statuses = Arrays.asList(PostStatus.SELLING, PostStatus.SOLD);
        } else if (type == PostType.BUY) {
            statuses = Arrays.asList(PostStatus.BUYING, PostStatus.BOUGHT);
        } else {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<MyPostsDTO> responses = userService.getUserPostsByType(email, type, statuses);
        return ResponseEntity.ok(responses);
    }

    //상태 변경
    /**
     * 판매중 -> 판매완료, 판매완료 -> 판매중, 구매중 -> 구매완료, 구매완료 -> 구매중
     */
    @PatchMapping("/profile/myposts/{postId}/toggle-status")
    public ResponseEntity<String> togglePostStatus(
            Authentication authentication,
            @PathVariable("postId") Long postId) {
        String email = authentication.getName();

        // 사용자 정보 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 상태 변경 후 메시지 반환
        String message = userService.togglePostStatus(postId, user.getId());

        // 상태 변경 완료 메시지 응답
        return ResponseEntity.ok(message);
    }

}
