package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Boolean existsRecipeByName(String name);
}