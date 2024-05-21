package kg.neobis.cookscorner.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.dto.RecipeDetailPageDto;
import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.dto.PageRecipeDto;
import kg.neobis.cookscorner.dto.RecipeSearchPageDto;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.enums.Category;
import kg.neobis.cookscorner.enums.Difficulty;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.service.RecipeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/create")
    public ResponseEntity<?> createRecipe(@RequestParam("file") MultipartFile file,
                                          @RequestParam("name") String name,
                                          @RequestParam("description") String description,
                                          @RequestParam("difficulty") Difficulty difficulty,
                                          @RequestParam("category") Category category,
                                          @RequestParam("preparation_time") String preparation_time,
                                          @RequestParam("username") String username,
                                          @RequestParam("ingredients") String ingredientsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Ingredient> ingredients = objectMapper.readValue(ingredientsJson, new TypeReference<>() {
            });
            RecipeDto createdRecipe = service.createRecipe(file, name, description, difficulty, category, preparation_time, username, ingredients);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create recipe: " + e.getMessage());
        }
    }

    @GetMapping("/category")
    public ResponseEntity<List<PageRecipeDto>> getRecipesByCategory(@RequestParam Category category, @RequestParam String authenticatedUsername) {
        List<PageRecipeDto> recipes = service.getRecipesByCategory(category, authenticatedUsername);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/details")
    public ResponseEntity<RecipeDetailPageDto> getRecipeDetails(@RequestParam String recipeName, @RequestParam String authenticatedUsername) {
        RecipeDetailPageDto recipeDetails = service.getRecipeDetails(recipeName, authenticatedUsername);
        return ResponseEntity.ok(recipeDetails);
    }

    @GetMapping("/user-recipes")
    public ResponseEntity<?> getUserRecipes(@RequestParam String username) {
        List<PageRecipeDto> recipes = service.getUserRecipes(username);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/user-saved-recipes")
    public ResponseEntity<?> getUserSavedRecipes(@RequestParam String authenticatedUsername) {
        List<PageRecipeDto> recipes = service.getSavedRecipes(authenticatedUsername);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeSearchPageDto>> searchRecipes(@RequestParam String recipeName) {
        List<RecipeSearchPageDto> recipes = service.searchRecipesByName(recipeName);
        return ResponseEntity.ok(recipes);
    }

    // LIKE

    @PostMapping("/like")
    public ResponseEntity<?> likeRecipe(@RequestParam String authenticatedUsername, @RequestParam String recipeName) {
        try {
            service.likeRecipe(authenticatedUsername, recipeName);
            return ResponseEntity.ok("Recipe liked/unliked successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to like/unlike recipe: " + e.getMessage());
        }
    }

    @GetMapping("/like/status")
    public ResponseEntity<Boolean> isRecipeLikedByUser(@RequestParam String authenticatedUsername, @RequestParam String recipeName) {
        Boolean isLiked = service.isRecipeLikedByUser(authenticatedUsername, recipeName);
        return ResponseEntity.ok(isLiked);
    }

    // SAVE

    @PostMapping("/save")
    public ResponseEntity<?> saveRecipe(@RequestParam String authenticatedUsername, @RequestParam String recipeName) {
        try {
            service.saveRecipe(authenticatedUsername, recipeName);
            return ResponseEntity.ok("Recipe saved/unsaved successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save/unsave recipe: " + e.getMessage());
        }
    }

    @GetMapping("/save/status")
    public ResponseEntity<Boolean> isRecipeSavedByUser(@RequestParam String authenticatedUsername, @RequestParam String recipeName) {
        Boolean isSaved = service.isRecipeSavedByUser(authenticatedUsername, recipeName);
        return ResponseEntity.ok(isSaved);
    }
}