package kg.neobis.cookscorner.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.dto.RecipeDto;
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
import org.springframework.web.bind.annotation.*;
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
}