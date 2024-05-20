package kg.neobis.cookscorner.service;

import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.enums.Category;
import kg.neobis.cookscorner.enums.Difficulty;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecipeService {

    RecipeDto createRecipe(MultipartFile file, String name, String description, Difficulty difficulty, Category category,
                           String preparation_time, Long userId, List<Ingredient> ingredients) throws IOException;
}