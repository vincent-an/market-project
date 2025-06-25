package likeLion2025.Left.domain.post.entity;

import jakarta.persistence.*;
import likeLion2025.Left.domain.category.entity.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import likeLion2025.Left.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title; //제목

    @Column(nullable = false)
    private String content; //글 내용

    @Column(nullable = false)
    private String photoUrl; //사진 url

    @Column(nullable = false)
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

    @Column(nullable = false)
    private String contactLink; //카톡 오픈톡방 링크

    private LocalDateTime createdAt; //작성 시간, 2025-06-25T02:32:00 처럼 출력

    public String getNickname() {
        return this.user.getNickname(); //user 테이블에서 nickName 참조
    }
}
