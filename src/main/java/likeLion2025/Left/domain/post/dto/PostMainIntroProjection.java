package likeLion2025.Left.domain.post.dto;

import java.time.LocalDateTime;

public interface PostMainIntroProjection {
    Long getId();
    String getIntroImgUrl();
    String getTitle();
    int getPrice();
    String getContent();
    String getCategory();
    LocalDateTime getCreatedAt();
}
