package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.entity.LikedRecipe;
import kg.neobis.cookscorner.entity.Recipe;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.exception.ResourceAlreadyExistsException;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.repository.LikedRecipeRepository;
import kg.neobis.cookscorner.repository.RecipeRepository;
import kg.neobis.cookscorner.repository.UserRepository;
import kg.neobis.cookscorner.service.LikedRecipeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikedRecipeServiceImpl implements LikedRecipeService {

    LikedRecipeRepository likedRecipeRepository;
    UserRepository userRepository;
    RecipeRepository recipeRepository;

    @Override
    public void likeRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        if (likedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent()) {
            throw new ResourceAlreadyExistsException("Recipe '" + recipe.getName() + "' already liked by '" + user.getUsername() + "'", HttpStatus.CONFLICT.value());
        }

        LikedRecipe likedRecipe = new LikedRecipe();
        likedRecipe.setUser(user);
        likedRecipe.setRecipe(recipe);
        likedRecipeRepository.save(likedRecipe);
    }

    @Override
    public void unlikeRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        LikedRecipe likedRecipe = likedRecipeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe '" + recipe.getName() + "' wasn't liked by '" + user.getUsername() + "'", HttpStatus.NOT_FOUND.value()));

        likedRecipeRepository.delete(likedRecipe);
    }

    @Override
    public boolean isRecipeLikedByUser(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        return likedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent();
    }
}
