package likeLion2025.Left.domain.post.service;

import likeLion2025.Left.domain.post.dto.*;
import likeLion2025.Left.domain.post.dto.request.PostRequest;
import likeLion2025.Left.domain.post.dto.request.PostUpdateRequest;
import likeLion2025.Left.domain.post.dto.response.PostMainIntroResponse;
import likeLion2025.Left.domain.post.dto.response.PostResponse;
import likeLion2025.Left.domain.post.dto.response.PostUpdateResponse;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.post.entity.enums.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.post.repository.PostRepository;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.repository.UserRepository;
import likeLion2025.Left.grobal.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;
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

    // 게시글 수정
    @Transactional
    public PostUpdateResponse updatePost(Long postId, Long userId, PostUpdateRequest postUpdateRequest) {
        // 게시글 조회 및 권한 확인
        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없거나 권한이 없습니다."));

        // 게시글 수정
        post.setTitle(postUpdateRequest.getTitle());
        post.setContent(postUpdateRequest.getContent());
        post.setPrice(postUpdateRequest.getPrice());
        post.setCategory(Category.valueOf(postUpdateRequest.getCategory().toUpperCase()));
        post.setReturnable(postUpdateRequest.isReturnable());
        post.setDelivery(postUpdateRequest.isDelivery());
        post.setDirectTrade(postUpdateRequest.isDirectTrade());
        post.setContactLink(postUpdateRequest.getContactLink());
        post.setPostType(PostType.valueOf(postUpdateRequest.getPostType().toUpperCase()));
        post.setStatus(PostStatus.valueOf(postUpdateRequest.getPostStatus().toUpperCase()));
        // 수정된 게시글 저장
        postRepository.save(post);

        // 수정된 게시글 정보 반환 (imgUrls만 응답)
        return PostUpdateResponse.from(post, "게시글을 수정했습니다.");
    }

    //전체 게시글 조회 + 검색 키워드가 있으면 키워드로 검색
    public List<PostMainIntroResponse> selectPosts(String keyword) {
        List<PostMainIntroProjection> projections;

        if(keyword == null || keyword.isBlank()) {
            projections = postRepository.findAllBy();
        } else {
            projections = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
        }

        return projections.stream()
                .map(p -> PostMainIntroResponse.builder()
                        .introImgUrl(p.getIntroImgUrl())
                        .title(p.getTitle())
                        .price(p.getPrice())
                        .category(p.getCategory())
                        .build()
                ).collect(Collectors.toList());
    }

    // 세부 게시글 조회
    public PostResponse selsectOnePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 없습니다."));
        return PostResponse.from(post,"게시글 상세 조회 성공");
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // 작성자 확인
        if (!post.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        // 이미지 삭제
        imageService.deleteImages(post.getImgUrls());
        // 게시글 삭제
        postRepository.delete(post);
    }

    // PostType 필터링 기능
    public List<PostMainIntroResponse> filterByTypeAndStatus(PostType postType, PostStatus postStatus) {
        List<Post> posts = postRepository.findByPostTypeAndStatus(postType, postStatus);

        return posts.stream()
                .map(post -> PostMainIntroResponse.builder()
                        .introImgUrl(post.getIntroImgUrl())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .category(post.getCategory().getValue())
                        .build())
                .collect(Collectors.toList());
    }
    // 카테고리와 상태에 맞는 게시글을 조회하는 로직
    public List<PostMainIntroResponse> filterByCategoryAndStatus(Category categoryEnum, List<PostStatus> statuses) {
        List<Post> posts = postRepository.findByCategoryAndStatusIn(categoryEnum, statuses);

        return posts.stream()
                .map(post -> PostMainIntroResponse.builder()
                        .introImgUrl(post.getIntroImgUrl())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .category(post.getCategory().getValue())
                        .build())
                .collect(Collectors.toList());
    }

}
