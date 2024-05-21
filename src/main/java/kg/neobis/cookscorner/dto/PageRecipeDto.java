package kg.neobis.cookscorner.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageRecipeDto {
    Long recipeId;
    String recipeName;
    String imageUrl;
    String authorName;
    Long likeCount;
    Long saveCount;
    Boolean isLikedByCurrentUser;
    Boolean isSavedByCurrentUser;
}