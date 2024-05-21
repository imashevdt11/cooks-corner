package kg.neobis.cookscorner.service;

import kg.neobis.cookscorner.dto.RecipeDetailPageDto;
import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.dto.PageRecipeDto;
import kg.neobis.cookscorner.dto.RecipeSearchPageDto;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.enums.Category;
import kg.neobis.cookscorner.enums.Difficulty;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecipeService {

    RecipeDto createRecipe(MultipartFile file, String name, String description, Difficulty difficulty, Category category,
                           String preparation_time, Long userId, List<Ingredient> ingredients) throws IOException;

    List<PageRecipeDto> getRecipesByCategory(Category category, Long currentUserId);

    RecipeDetailPageDto getRecipeDetails(Long recipeId, Long currentUserId);

    List<PageRecipeDto> getUserRecipes(Long userId);

    List<PageRecipeDto> getSavedRecipes(Long userId);

    List<RecipeSearchPageDto> searchRecipesByName(String name);
}