package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByRecipe(Recipe recipe);
}
