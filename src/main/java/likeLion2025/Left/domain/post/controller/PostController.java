package likeLion2025.Left.domain.post.controller;

import likeLion2025.Left.domain.post.dto.response.PostMainIntroResponse;
import likeLion2025.Left.domain.post.dto.request.PostRequest;
import likeLion2025.Left.domain.post.dto.response.PostResponse;
import likeLion2025.Left.domain.post.dto.request.PostUpdateRequest;
import likeLion2025.Left.domain.post.dto.response.PostUpdateResponse;
import likeLion2025.Left.domain.post.entity.enums.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.post.service.PostService;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
        //토큰 인증
        String email = authentication.getName();

        // 3. DB에서 User 조회 (없으면 예외 발생)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 4. userId 꺼내기
        Long userId = user.getId();

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

    // 세부 게시글 조회
    @GetMapping("/content/{post_id}")
    public ResponseEntity<PostResponse> contentPage(@PathVariable("post_id") Long postId) {
        PostResponse response = postService.selsectOnePost(postId);
        return ResponseEntity.ok(response);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable("post_id") Long postId, Authentication authentication) {
        String email = authentication.getName();
        try {
            postService.deletePost(postId, email);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 삭제 실패: " + e.getMessage());
        }
    }

    //게시글 수정
    @PutMapping("/edit/{post_id}")
    public ResponseEntity<PostUpdateResponse> updatePost(
            Authentication authentication,
            @PathVariable("post_id") Long postId,
            @RequestBody PostUpdateRequest request) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        PostUpdateResponse response = postService.updatePost(postId, user.getId(), request);

        return ResponseEntity.ok(response);
    }

    // SELL or BUY 필터링
    @GetMapping("/list/type/{postType}")
    public ResponseEntity<List<PostMainIntroResponse>> filterByType(
            @PathVariable("postType") String postType) {

        PostType postTypeEnum;
        try {
            postTypeEnum = PostType.valueOf(postType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.info("해당 postType이 존재하지 않습니다.");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        // 'SELL'이면 SELLING, 'BUY'이면 BUYING 상태로 고정
        PostStatus postStatus = postTypeEnum == PostType.SELL ? PostStatus.SELLING : PostStatus.BUYING;

        List<PostMainIntroResponse> responses = postService.filterByTypeAndStatus(postTypeEnum, postStatus);
        log.info("필터링을 시작합니다. 기준: {}, 상태: {}", postTypeEnum, postStatus);
        return ResponseEntity.ok(responses);
    }
    // 카테고리 필터링
    @GetMapping("/list/category/{category}")
    public ResponseEntity<List<PostMainIntroResponse>> filterByCategory(
            @PathVariable("category") String category) {

        Category categoryEnum;
        try {
            categoryEnum = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.info("해당 category가 존재하지 않습니다.");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        // 판매중과 구매중만 포함하게 필터링
        List<PostStatus> statuses = Arrays.asList(PostStatus.SELLING, PostStatus.BUYING);
        List<PostMainIntroResponse> responses = postService.filterByCategoryAndStatus(categoryEnum, statuses);

        log.info("카테고리 필터링을 시작합니다. 기준: {}", categoryEnum);
        return ResponseEntity.ok(responses);
    }

}
