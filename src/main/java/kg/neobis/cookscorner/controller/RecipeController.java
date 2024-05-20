package kg.neobis.cookscorner.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.dto.RecipeDetailPageDto;
import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.dto.RecipeMainPageDto;
import kg.neobis.cookscorner.dto.RecipeSearchPageDto;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.enums.Category;
import kg.neobis.cookscorner.enums.Difficulty;
import kg.neobis.cookscorner.service.RecipeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe")
@RequestMapping(EndpointConstants.RECIPE_ENDPOINT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecipeController {

    RecipeService service;

    @Operation(summary = "create recipe")
    @PostMapping("/create")
    public ResponseEntity<?> createRecipe(@RequestParam("file") MultipartFile file,
                                          @RequestParam("name") String name,
                                          @RequestParam("description") String description,
                                          @RequestParam("difficulty") Difficulty difficulty,
                                          @RequestParam("category") Category category,
                                          @RequestParam("preparation_time") String preparation_time,
                                          @RequestParam("userId") Long userId,
                                          @RequestParam("ingredients") String ingredientsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Ingredient> ingredients = objectMapper.readValue(ingredientsJson, new TypeReference<>() {
            });
            RecipeDto createdRecipe = service.createRecipe(file, name, description, difficulty, category, preparation_time, userId, ingredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create recipe: " + e.getMessage());
        }
    }

    @Operation(description = "get recipes with specified category")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<RecipeMainPageDto>> getRecipesByCategory(@PathVariable Category category, @RequestParam Long currentUserId) {
        List<RecipeMainPageDto> recipes = service.getRecipesByCategory(category, currentUserId);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/details")
    public ResponseEntity<RecipeDetailPageDto> getRecipeDetails(@RequestParam Long recipeId, @RequestParam Long currentUserId) {
        RecipeDetailPageDto recipeDetails = service.getRecipeDetails(recipeId, currentUserId);
        return ResponseEntity.ok(recipeDetails);
    }

    @Operation(summary = "search recipes by name")
    @GetMapping("/search")
    public ResponseEntity<List<RecipeSearchPageDto>> searchRecipes(@RequestParam("name") String name) {
        List<RecipeSearchPageDto> recipes = service.searchRecipesByName(name);
        return ResponseEntity.ok(recipes);
    }
}