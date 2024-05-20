package kg.neobis.cookscorner.mapper;

import kg.neobis.cookscorner.dto.ImageDto;
import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.dto.RecipeSearchPageDto;
import kg.neobis.cookscorner.entity.Image;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.entity.Recipe;

import java.util.List;

public class RecipeMapper {
    public static RecipeDto toRecipeDto(Recipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients().stream()
                .map(ingredient -> new Ingredient(ingredient.getId(), ingredient.getName(), ingredient.getAmount(), ingredient.getUnit()))
                .toList();

        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .image(recipe.getImage())
                .description(recipe.getDescription())
                .category(recipe.getCategory())
                .difficulty(recipe.getDifficulty())
                .preparation_time(recipe.getPreparation_time())
                .user(recipe.getUser())
                .ingredients(ingredients)
                .build();
    }
    public static RecipeSearchPageDto toRecipeSearchPageDto(Recipe recipe) {
        Image image = recipe.getImage();
        ImageDto imageDto = new ImageDto(image.getId(), image.getUrl());
        return new RecipeSearchPageDto(recipe.getId(), recipe.getName(), imageDto);
    }
}