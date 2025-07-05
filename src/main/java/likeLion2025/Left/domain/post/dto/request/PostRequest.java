package likeLion2025.Left.domain.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
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
    private String introImgUrl;// 대표 이미지 URL
    private List<String> imgUrls;// 전체 이미지 URL 리스트

}
