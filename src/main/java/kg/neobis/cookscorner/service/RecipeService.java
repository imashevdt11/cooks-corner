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
                           String preparation_time, String username, List<Ingredient> ingredients) throws IOException;

    RecipeDetailPageDto getRecipeDetails(String recipeName, String username);

    List<PageRecipeDto> getRecipesByCategory(Category category, String username);

    List<PageRecipeDto> getUserRecipes(String username);

    List<PageRecipeDto> getSavedRecipes(String username);

    List<RecipeSearchPageDto> searchRecipesByName(String name);


    // LIKE

    void likeRecipe(String username, String recipeName);

    boolean isRecipeLikedByUser(String username, String recipeName);


    // SAVE

    void saveRecipe(String username, String recipeName);

    boolean isRecipeSavedByUser(String username, String recipeName);
}