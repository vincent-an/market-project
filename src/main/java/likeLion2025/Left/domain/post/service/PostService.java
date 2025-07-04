package likeLion2025.Left.domain.post.service;

import likeLion2025.Left.domain.post.dto.PostMainIntroProjection;
import likeLion2025.Left.domain.post.dto.PostMainIntroResponse;
import likeLion2025.Left.domain.post.dto.PostRequest;
import likeLion2025.Left.domain.post.dto.PostResponse;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.post.entity.enums.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.post.repository.PostRepository;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 저장 메서드
    @Transactional
    public PostResponse createPost(PostRequest request, User user) {
//        // 사용자 정보 가져오기
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        log.info("User ID: {}", user.getId());

        // 카테고리 정보 가져오기
        Category category;
        try {
            category = Category.valueOf(request.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("잘못된 카테고리 값입니다.");
        }

        PostStatus postStatus;
        if ("BUY".equalsIgnoreCase(request.getPostType())) {
            postStatus = PostStatus.BUYING;
        } else if ("SELL".equalsIgnoreCase(request.getPostType())) {
            postStatus = PostStatus.SELLING;
        } else {
            throw new RuntimeException("post type을 제대로 입력해주세요.");
        }

        Post post = Post.builder()
                .user(user)          // 사용자 정보 연결
                .title(request.getTitle())
                .imgUrls(request.getImgUrls())
                .introImgUrl(request.getIntroImgUrl())
                .content(request.getContent())
                .price(request.getPrice())
                .category(Category.valueOf(request.getCategory()))
                .isReturnable(request.isReturnable())
                .isDelivery(request.isDelivery())
                .isDirectTrade(request.isDirectTrade())
                .contactLink(request.getContactLink())
                .status(postStatus)
                .createdAt(LocalDateTime.now())
                .postType(PostType.valueOf(request.getPostType().toUpperCase()))
                .build();

        Post newPost = postRepository.save(post);
        log.info("Post created: id={}, title={}", newPost.getId(), newPost.getTitle());
        return PostResponse.from(newPost, "상품이 성공적으로 등록되었습니다.");
    }

    //전체 게시글 조회
    public List<PostMainIntroResponse> selectPosts() {
        List<PostMainIntroProjection> projections = postRepository.findAllBy();

        return projections.stream()
                .map(p -> new PostMainIntroResponse(
                        p.getIntroImgUrl(),
                        p.getTitle(),
                        p.getPrice(),
                        p.getCategory()
                ))
                .collect(Collectors.toList());
    }
}
