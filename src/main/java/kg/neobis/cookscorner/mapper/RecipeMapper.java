package kg.neobis.cookscorner.mapper;

import kg.neobis.cookscorner.dto.ImageDto;
import kg.neobis.cookscorner.dto.IngredientDto;
import kg.neobis.cookscorner.dto.RecipeDto;
import kg.neobis.cookscorner.dto.RecipeSearchPageDto;
import kg.neobis.cookscorner.entity.Image;
import kg.neobis.cookscorner.entity.Recipe;

import java.util.List;

public class RecipeMapper {
    public static RecipeDto toRecipeDto(Recipe recipe) {
        List<IngredientDto> ingredients = recipe.getIngredients().stream()
                .map(ingredient -> new IngredientDto(ingredient.getName(), ingredient.getAmount(), ingredient.getUnit()))
                .toList();

        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .imageUrl(recipe.getImage().getUrl())
                .description(recipe.getDescription())
                .category(recipe.getCategory())
                .difficulty(recipe.getDifficulty())
                .preparation_time(recipe.getPreparation_time())
                .username(recipe.getUser().getUsername())
                .ingredients(ingredients)
                .build();
    }
    public static RecipeSearchPageDto toRecipeSearchPageDto(Recipe recipe) {
        Image image = recipe.getImage();
        ImageDto imageDto = new ImageDto(image.getId(), image.getUrl());
        return new RecipeSearchPageDto(recipe.getId(), recipe.getName(), imageDto);
    }
}