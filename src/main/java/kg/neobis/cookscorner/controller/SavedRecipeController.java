package kg.neobis.cookscorner.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.service.SavedRecipeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Saved Recipes")
@RequestMapping(EndpointConstants.SAVE_RECIPE_ENDPOINT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SavedRecipeController {

    SavedRecipeService service;

    @PostMapping
    public ResponseEntity<String> saveRecipe(@RequestParam Long userId, @RequestParam Long recipeId) {
        service.saveRecipe(userId, recipeId);
        return ResponseEntity.ok("recipe was saved");
    }

    @DeleteMapping
    public ResponseEntity<String> unsaveRecipe(@RequestParam Long userId, @RequestParam Long recipeId) {
        service.unsaveRecipe(userId, recipeId);
        return ResponseEntity.ok("recipe was unsaved");
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> isRecipeSavedByUser(@RequestParam Long userId, @RequestParam Long recipeId) {
        Boolean isSaved = service.isRecipeSavedByUser(userId, recipeId);
        return ResponseEntity.ok(isSaved);
    }
}