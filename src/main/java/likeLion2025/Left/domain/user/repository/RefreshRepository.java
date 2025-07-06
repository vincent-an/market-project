package likeLion2025.Left.domain.user.repository;

import jakarta.transaction.Transactional;
import likeLion2025.Left.domain.user.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {
    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
