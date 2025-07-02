package likeLion2025.Left.domain.post.repository;

import likeLion2025.Left.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
