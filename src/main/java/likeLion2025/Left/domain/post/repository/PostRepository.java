package likeLion2025.Left.domain.post.repository;

import likeLion2025.Left.domain.post.dto.PostMainIntroProjection;
import likeLion2025.Left.domain.post.entity.Post;
import likeLion2025.Left.domain.post.entity.enums.Category;
import likeLion2025.Left.domain.post.entity.enums.PostStatus;
import likeLion2025.Left.domain.post.entity.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<PostMainIntroProjection> findAllByOrderByCreatedAtDesc();
    Optional<Post> findById(Long postId);
    List<Post> findByPostTypeAndStatusOrderByCreatedAtDesc(PostType postType, PostStatus postStatus);
    List<Post> findByCategoryAndStatusInOrderByCreatedAtDesc(Category category, List<PostStatus> statuses);
    List<Post> findByUserEmailAndPostTypeAndStatusInOrderByCreatedAtDesc(String email, PostType type, List<PostStatus> statuses);
    Optional<Post> findByIdAndUserId(Long postId, Long userId);
    // 검색기능
    List<PostMainIntroProjection> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(String title, String content);

}
