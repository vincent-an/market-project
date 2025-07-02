package likeLion2025.Left.domain.post.dto;

import likeLion2025.Left.domain.category.entity.Category;
import likeLion2025.Left.domain.category.entity.enums.CategoryName;
import likeLion2025.Left.domain.category.repository.CategoryRepository;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreatePostRequest {
    private String title;
    private String content;
    private int price;
    private String photoUrl;
    private String categoryName;
    private boolean isReturnable;
    private boolean isDelivery;
    private boolean isDirectTrade;
    private String contactLink;
    private String postType;

//    public Post saveEntity(CategoryRepository categoryRepository, User user) {
//        // categoryName으로 Category 엔티티 조회
//        Category category = categoryRepository.findByCategoryName(CategoryName.valueOf(this.categoryName))
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        // Post 엔티티 생성
//        return Post.builder()
//                .title(this.title)
//                .content(this.content)
//                .price(this.price)
//                .photoUrl(this.photoUrl)
//                .category(category)
//                .user(user)
//                .status(PostStatus.SELLING)
//                .isReturnable(this.isReturnable)
//                .isDelivery(this.isDelivery)
//                .isDirectTrade(this.isDirectTrade)
//                .contactLink(this.contactLink)
//                .createdAt(LocalDateTime.now()) // 작성시간 자동 설정
//                .build();
//    }
}
