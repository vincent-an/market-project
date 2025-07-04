package likeLion2025.Left.domain.post.repository;

import likeLion2025.Left.domain.post.dto.PostMainIntroProjection;
import likeLion2025.Left.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import likeLion2025.Left.domain.post.entity.enums.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<PostMainIntroProjection> findAllBy();
    List<Post> findByCategory(Category category);
    List<Post> findByStatus(PostStatus status);
    List<Post> findByPostType(PostType postType);
}
