package kg.neobis.cookscorner.service;

public interface SavedRecipeService {

    void saveRecipe(Long userId, Long recipeId);

    void unsaveRecipe(Long userId, Long recipeId);

    boolean isRecipeSavedByUser(Long userId, Long recipeId);
}