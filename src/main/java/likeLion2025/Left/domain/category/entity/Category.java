package likeLion2025.Left.domain.category.entity;

import jakarta.persistence.*;
import likeLion2025.Left.domain.category.entity.enums.CategoryName;
import likeLion2025.Left.domain.post.entity.Post;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;
}
