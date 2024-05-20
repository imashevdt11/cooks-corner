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
    Long id;
    String name;
    String preparationTime;
    Difficulty difficulty;
    Long userId;
    String description;
    String imageUrl;
    String username;
    List<IngredientDto> ingredients;
    Long likeCount;
    Boolean isLikedByCurrentUser;
    Boolean isSavedByCurrentUser;
}