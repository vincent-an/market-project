package likeLion2025.Left.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostMainIntroResponse {
    private Long postId;
    private String introImgUrl;
    private String title;
    private int price;
    private String category;
}
