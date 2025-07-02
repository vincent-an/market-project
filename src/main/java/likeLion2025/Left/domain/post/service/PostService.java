package likeLion2025.Left.domain.post.service;

import likeLion2025.Left.domain.category.entity.Category;
import likeLion2025.Left.domain.category.entity.enums.CategoryName;
import likeLion2025.Left.domain.category.repository.CategoryRepository;
import likeLion2025.Left.domain.post.dto.CreatePostRequest;
import likeLion2025.Left.domain.post.dto.PostResponse;
import likeLion2025.Left.domain.post.entity.Post;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public PostResponse createPost(CreatePostRequest request, Long userId) {
        // 사용자 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        // 카테고리 정보 가져오기
        Category category = categoryRepository.findByCategoryName(CategoryName.valueOf(request.getCategoryName()))
                .orElseThrow(() -> new RuntimeException("Category not found"));
        //이미지 업로드 코드
        // String photoUrl = s3Service.uploadFile(request.getPhotoUrl());

        PostStatus postStatus;
        if ("BUY".equalsIgnoreCase(request.getPostType())) {
            postStatus = PostStatus.BUYING;
        } else if ("SELL".equalsIgnoreCase(request.getPostType())) {
            postStatus = PostStatus.SELLING;
        } else {
            throw new RuntimeException("post type을 제대로 입력해주세요.");
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .photoUrl(request.getPhotoUrl())
                .category(category) // Category 엔티티 연결
                .user(user)          // 사용자 정보 연결
                .isReturnable(request.isReturnable())
                .isDelivery(request.isDelivery())
                .isDirectTrade(request.isDirectTrade())
                .contactLink(request.getContactLink())
                .status(postStatus)
                .createdAt(LocalDateTime.now())
                .postType(PostType.valueOf(request.getPostType().toUpperCase()))
                .build();

        Post newPost = postRepository.save(post);
        return PostResponse.from(newPost, "상품이 성공적으로 등록되었습니다.");


    }
}
