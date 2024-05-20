package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Recipe;
import kg.neobis.cookscorner.entity.SavedRecipe;
import kg.neobis.cookscorner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {

    Optional<SavedRecipe> findByUserAndRecipe(User user, Recipe recipe);

    Long countByRecipe(Recipe recipe);
}