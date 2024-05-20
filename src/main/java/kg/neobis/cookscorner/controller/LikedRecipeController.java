package kg.neobis.cookscorner.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.service.LikedRecipeService;
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
@Tag(name = "Liked Recipes")
@RequestMapping(EndpointConstants.LIKE_RECIPE_ENDPOINT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikedRecipeController {

    LikedRecipeService service;

    @PostMapping
    public ResponseEntity<String> likeRecipe(@RequestParam Long userId, @RequestParam Long recipeId) {
        service.likeRecipe(userId, recipeId);
        return ResponseEntity.ok("recipe was liked");
    }

    @DeleteMapping
    public ResponseEntity<String> unlikeRecipe(@RequestParam Long userId, @RequestParam Long recipeId) {
        service.unlikeRecipe(userId, recipeId);
        return ResponseEntity.ok("recipe was unliked");
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> isRecipeLikedByUser(@RequestParam Long userId, @RequestParam Long recipeId) {
        Boolean isLiked = service.isRecipeLikedByUser(userId, recipeId);
        return ResponseEntity.ok(isLiked);
    }
}