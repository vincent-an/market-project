package likeLion2025.Left.domain.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequest {

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

    //현재는 이미지 변경은 구현X, 추후 구현 예정
//    private String introImgUrl; // 대표 이미지 URL
//
//    private List<String> originalImageUrls; // 유지할 기존 이미지 URL 리스트
//
//    private List<MultipartFile> newImages; // 새로 업로드할 이미지 파일 리스트
}

