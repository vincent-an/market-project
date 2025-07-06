package likeLion2025.Left.domain.user.dto.response;

import likeLion2025.Left.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkResponse {
    private Long userId;
    private Long postId;
    private String message;

    public static BookmarkResponse from(User user, Long postId, String message) {
        return BookmarkResponse.builder()
                .userId(user.getId())
                .postId(postId)
                .message(message)
                .build();
    }
}
