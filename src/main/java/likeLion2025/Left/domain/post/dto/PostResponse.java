package likeLion2025.Left.domain.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import likeLion2025.Left.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private int price;
    private String photoUrl;
    private Long categoryId;
    private String categoryName;
    private boolean isReturnable;
    private boolean isDelivery;
    private boolean isDirectTrade;
    private String contactLink;
    private String message;
    private String postType;
    private String postStatus;

    public static PostResponse from(Post post, String message) {
        return PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .price(post.getPrice())
                .photoUrl(post.getPhotoUrl())
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategoryName())
                .isReturnable(post.isReturnable())
                .isDelivery(post.isDelivery())
                .isDirectTrade(post.isDirectTrade())
                .contactLink(post.getContactLink())
                .postType(post.getPostType().getValue())
                .postStatus(post.getStatus().getValue())
                .message(message)
                .build();
    }
}
