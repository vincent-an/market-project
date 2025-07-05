package likeLion2025.Left.domain.post.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import likeLion2025.Left.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponse {
    private Long postId;
    private Long userId;
    private String nickName;
    private String title;
    private String content;
    private int price;
    private String category;
    private boolean isReturnable;
    private boolean isDelivery;
    private boolean isDirectTrade;
    private String contactLink;
    private String message;
    private String postType;
    private String postStatus;

    // 이미지 관련 필드 추가
    private String introImgUrl;
    private List<String> imgUrls;

    public static PostResponse from(Post post, String message) {
        return PostResponse.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .nickName(post.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .price(post.getPrice())
                .category(post.getCategory().getValue())
                .isReturnable(post.isReturnable())
                .isDelivery(post.isDelivery())
                .isDirectTrade(post.isDirectTrade())
                .contactLink(post.getContactLink())
                .postType(post.getPostType().getValue())
                .postStatus(post.getStatus().getValue())
                .introImgUrl(post.getIntroImgUrl())
                .imgUrls(post.getImgUrls())
                .message(message)
                .build();
    }
}

