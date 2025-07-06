package likeLion2025.Left.domain.post.dto.response;

import likeLion2025.Left.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostUpdateResponse {
    private Long postId;
    private String title;
    private String content;
    private int price;
    private String category;
    private boolean isReturnable;
    private boolean isDelivery;
    private boolean isDirectTrade;
    private String contactLink;
    private String postType;
    private String postStatus;
    private List<String> imgUrls;
    private String message;

    public static PostUpdateResponse from(Post post, String message) {
        return PostUpdateResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .price(post.getPrice())
                .category(post.getCategory().getValue()) // Enum -> String 변환
                .isReturnable(post.isReturnable())
                .isDelivery(post.isDelivery())
                .isDirectTrade(post.isDirectTrade())
                .contactLink(post.getContactLink())
                .postType(post.getPostType().getValue())
                .postStatus(post.getStatus().getValue())
                .imgUrls(post.getImgUrls())
                .message(message)
                .build();
    }
}

