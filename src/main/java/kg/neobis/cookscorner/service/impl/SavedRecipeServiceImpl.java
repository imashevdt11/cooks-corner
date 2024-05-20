package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.entity.Recipe;
import kg.neobis.cookscorner.entity.SavedRecipe;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.exception.ResourceAlreadyExistsException;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.repository.RecipeRepository;
import kg.neobis.cookscorner.repository.SavedRecipeRepository;
import kg.neobis.cookscorner.repository.UserRepository;
import kg.neobis.cookscorner.service.SavedRecipeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SavedRecipeServiceImpl implements SavedRecipeService {

    SavedRecipeRepository savedRecipeRepository;
    UserRepository userRepository;
    RecipeRepository recipeRepository;

    @Override
    public void saveRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        if (savedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent()) {
            throw new ResourceAlreadyExistsException("Recipe '" + recipe.getName() + "' already saved by '" + user.getUsername() + "'", HttpStatus.CONFLICT.value());
        }

        SavedRecipe savedRecipe = new SavedRecipe();
        savedRecipe.setUser(user);
        savedRecipe.setRecipe(recipe);
        savedRecipeRepository.save(savedRecipe);
    }

    @Override
    public void unsaveRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        SavedRecipe savedRecipe = savedRecipeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe '" + recipe.getName() + "' wasn't saved by '" + user.getUsername() + "'", HttpStatus.NOT_FOUND.value()));

        savedRecipeRepository.delete(savedRecipe);
    }

    @Override
    public boolean isRecipeSavedByUser(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND.value()));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId, HttpStatus.NOT_FOUND.value()));

        return savedRecipeRepository.findByUserAndRecipe(user, recipe).isPresent();
    }
}