package likeLion2025.Left.domain.post.controller;

import likeLion2025.Left.domain.post.dto.PostMainIntroResponse;
import likeLion2025.Left.domain.post.dto.PostRequest;
import likeLion2025.Left.domain.post.dto.PostResponse;
import likeLion2025.Left.domain.post.service.PostService;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/eushop")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    //게시글 작성
    @PostMapping("/create")
    public ResponseEntity<PostResponse> createPost(Authentication authentication,
                                                   @RequestBody PostRequest request) {
//        이미 Authentication authentication를 통해 받아와서 중복 사용 안해도 됨
//        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 3. DB에서 User 조회 (없으면 예외 발생)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 4. userId 꺼내기
        Long userId = user.getId();
//        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        log.info("User ID: {}", userId);
        log.info("request to POST postTitle: {}", request.getTitle());

        PostResponse response = postService.createPost(request, user);
        return ResponseEntity.ok(response);
    }

    //게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<PostMainIntroResponse>> listPosts(Model model) {
        List<PostMainIntroResponse> posts = postService.selectPosts();
        return ResponseEntity.ok(posts);
    }
}
