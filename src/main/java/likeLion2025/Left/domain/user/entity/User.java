package likeLion2025.Left.domain.user.entity;

import jakarta.persistence.*;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.user.entity.enums.Department;
import likeLion2025.Left.domain.user.entity.enums.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_liked_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> likedPosts = new ArrayList<>(); // 찜 게시글
}


