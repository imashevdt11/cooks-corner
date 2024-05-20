package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Recipe;
import kg.neobis.cookscorner.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Boolean existsRecipeByName(String name);

    List<Recipe> findByCategory(Category category);
}