package likeLion2025.Left.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPostsDTO {
    private String introImgUrl;
    private String title;
    private int price;
    private String category;
    private String status;
}
