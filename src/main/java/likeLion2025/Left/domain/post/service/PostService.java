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
import org.springframework.security.access.AccessDeniedException;
import likeLion2025.Left.domain.post.entity.enums.Category;

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
                .user(user)
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
//

        /**
         * 게시글 수정
         * - 게시글 ID와 요청 데이터를 받아 게시글을 수정
         * - 로그인한 사용자가 작성자인 경우에만 수정 가능
         * @param postId   수정할 게시글 ID
         * @param request  수정 요청 데이터
         * @param user     로그인한 사용자
         * @return 수정된 게시글의 응답 DTO
         * ??????모르겟어어어
         */
        @Transactional
        public PostResponse updatePost(Long postId, PostRequest request, User user) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

            if (!post.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("게시글을 수정할 권한이 없습니다.");
            }

            post.updateFrom(request);  // Post 엔티티의 필드를 DTO 기반으로 갱신
            Post updated = postRepository.save(post); // 변경된 post 저장
            return PostResponse.from(updated, "게시물이 수정되었습니다.");
        }

        /**
         * 카테고리 필터링_MAJOR, GENERAL, MISC
         * @param category 문자열로 받은 카테고리 값
         * @return 필터링된 게시물 리스트 (썸네일 + 제목 + 가격 + 카테고리)
         */
        public List<PostMainIntroResponse> filterByCategory(String category) {
            Category catEnum = Category.valueOf(category.toUpperCase());
            List<Post> posts = postRepository.findByCategory(catEnum);
            return posts.stream()
                    .map(p -> new PostMainIntroResponse(
                            p.getIntroImgUrl(),
                            p.getTitle(),
                            p.getPrice(),
                            p.getCategory().getValue()
                    ))
                    .collect(Collectors.toList());
        }

        /**
         * 상태 필터링_SELLING, SOLD, BUYING, BOUGHT
         * @param status 문자열로 받은 상태 값
         * @return 필터링된 게시물 리스트 (썸네일 + 제목 + 가격 + 카테고리)
         */
        public List<PostMainIntroResponse> filterByStatus(String status) {
            PostStatus statusEnum = PostStatus.valueOf(status.toUpperCase());
            List<Post> posts = postRepository.findByStatus(statusEnum);
            return posts.stream()
                    .map(p -> new PostMainIntroResponse(
                            p.getIntroImgUrl(),
                            p.getTitle(),
                            p.getPrice(),
                            p.getCategory().getValue()
                    ))
                    .collect(Collectors.toList());
        }

        /**
         * 글 종류 필터링_BUY 또는 SELL
         * @param postType 문자열로 받은 글 종류 값
         * @return 필터링된 게시물 리스트 (썸네일 + 제목 + 가격 + 카테고리)
         */
        public List<PostMainIntroResponse> filterByType(String postType) {
            PostType typeEnum = PostType.valueOf(postType.toUpperCase());
            List<Post> posts = postRepository.findByPostType(typeEnum);
            return posts.stream()
                    .map(p -> new PostMainIntroResponse(
                            p.getIntroImgUrl(),
                            p.getTitle(),
                            p.getPrice(),
                            p.getCategory().getValue()
                    ))
                    .collect(Collectors.toList());
        }

//

    // 전체 게시글 조회
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
