package kg.neobis.cookscorner.dto;

import kg.neobis.cookscorner.enums.Difficulty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDetailPageDto {
    Long recipeId;
    String recipeName;
    String preparationTime;
    Difficulty difficulty;
    String description;
    String imageUrl;
    Long authorId;
    String authorName;
    List<IngredientDto> ingredients;
    Long likeCount;
    Boolean isLikedByCurrentUser;
    Boolean isSavedByCurrentUser;
}