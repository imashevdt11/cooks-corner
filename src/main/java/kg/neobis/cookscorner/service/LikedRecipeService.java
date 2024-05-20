package kg.neobis.cookscorner.service;

public interface LikedRecipeService {

    void likeRecipe(Long userId, Long recipeId);

    void unlikeRecipe(Long userId, Long recipeId);

    boolean isRecipeLikedByUser(Long userId, Long recipeId);
}