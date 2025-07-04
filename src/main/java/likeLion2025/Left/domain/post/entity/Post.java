package likeLion2025.Left.domain.post.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import likeLion2025.Left.domain.post.entity.enums.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Title is required.")
    @Size(min = 3, max = 100, message = "제대로된 제목을 입력해주세요.")
    private String title; //제목

    @NotNull(message = "Content is required.")
    @Size(min = 1, message = "상품 설명을 적어주세요.")
    private String content; //글 내용

    private String introImgUrl; // 대표 이미지

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private List<String> imgUrls; // 전체 이미지 리스트

    @NotNull(message = "Price is required.")
    @Min(value = 0, message = "최소 금액은 0원 입니다.")
    private int price; //가격

    @Enumerated(EnumType.STRING)
    private PostStatus status; //상품상태 (판매 중, 판매 완료, 구매 중, 구매 완료)

    @Enumerated(EnumType.STRING)
    private PostType postType; //상품타입 (삽니다, 팝니다)

    @Column(nullable = false)
    private boolean isReturnable; //환불 여부

    @Column(nullable = false)
    private boolean isDelivery; //배송 여부

    @Column(nullable = false)
    private boolean isDirectTrade; //직거래 여부

    private boolean isLiked; //찜

    @NotNull(message = "Contact link is required.")
    private String contactLink; //카톡 오픈톡방 링크

    private LocalDateTime createdAt; //작성 시간, 2025-06-25T02:32:00 처럼 출력

    public String getNickname() {
        return this.user.getNickname(); //user 테이블에서 nickName 참조
    }
}
