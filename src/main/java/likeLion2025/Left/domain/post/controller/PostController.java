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

    // 게시글 작성
    @PostMapping("/create")
    public ResponseEntity<PostResponse> createPost(Authentication authentication,
                                                   @RequestBody PostRequest request) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Long userId = user.getId();

        log.info("User ID: {}", userId);
        log.info("request to POST postTitle: {}", request.getTitle());

        PostResponse response = postService.createPost(request, user);
        return ResponseEntity.ok(response);
    }

    //

        // 게시글 수정
        @PutMapping("/posts/{postId}")
        public ResponseEntity<PostResponse> updatePost(
                @PathVariable Long postId,
                @RequestBody PostRequest request,
                Authentication authentication
        ) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            PostResponse response = postService.updatePost(postId, request, user);
            return ResponseEntity.ok(response);
        }

        // 카테고리별 필터링 (전공, 교양, 잡화)
        @GetMapping("/posts/filter/category")
        public ResponseEntity<List<PostMainIntroResponse>> filterByCategory(
                @RequestParam String category
        ) {
            List<PostMainIntroResponse> result = postService.filterByCategory(category);
            return ResponseEntity.ok(result);
        }

        // 상태별 필터링 (SELLING, SOLD, BUYING, BOUGHT)
        @GetMapping("/posts/filter/status")
        public ResponseEntity<List<PostMainIntroResponse>> filterByStatus(
                @RequestParam String status
        ) {
            List<PostMainIntroResponse> result = postService.filterByStatus(status);
            return ResponseEntity.ok(result);
        }
        //put, 앤드포인트 수정

    // 타입별 필터링 (BUY -> BUYING, SELL -> SELLING 자동 적용됨)
    @GetMapping("/posts/filter/type")
    public ResponseEntity<List<PostMainIntroResponse>> filterByType(
            @RequestParam String postType
    ) {
        // 컨트롤러에서 postType을 대문자로 변환한 뒤 서비스에 넘김
        List<PostMainIntroResponse> result = postService.filterByTypeWithDefaultStatus(postType.toUpperCase());
        return ResponseEntity.ok(result);
    }

//

    // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<PostMainIntroResponse>> listPosts(Model model) {
        List<PostMainIntroResponse> posts = postService.selectPosts();
        return ResponseEntity.ok(posts);
    }
}
