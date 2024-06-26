package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Recipe;
import kg.neobis.cookscorner.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    int countByUserId(Long userId);

    Boolean existsRecipeByName(String name);

    List<Recipe> findByCategory(Category category);

    Optional<Recipe> findRecipeByName(String name);

    List<Recipe> findByUserId(Long userId);

    List<Recipe> findByNameStartingWithIgnoreCase(String name);
}