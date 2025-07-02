package likeLion2025.Left.domain.post.controller;

import likeLion2025.Left.domain.post.dto.CreatePostRequest;
import likeLion2025.Left.domain.post.dto.PostResponse;
import likeLion2025.Left.domain.post.service.PostService;
import likeLion2025.Left.domain.user.dto.CustomUserDetails;
import likeLion2025.Left.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/eushop")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponse> createPost(Authentication authentication, @RequestBody CreatePostRequest request) {
        Long userId =((CustomUserDetails)authentication.getPrincipal()).getId();
        log.info("request to POST postTitle: {}", request.getTitle());
        PostResponse response = postService.createPost(request, userId);
        return ResponseEntity.ok(response);
    }
}
