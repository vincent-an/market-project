package likeLion2025.Left.domain.category.repository;

import likeLion2025.Left.domain.category.entity.Category;
import likeLion2025.Left.domain.category.entity.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(CategoryName categoryName);

    Optional<Object> findById(CategoryName categoryName);
}
