package kg.neobis.cookscorner.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeMainPageDto {
    Long id;
    String name;
    Long imageId;
    String imageUrl;
    String userName;
    Long likeCount;
    Long saveCount;
    Boolean isLikedByCurrentUser;
    Boolean isSavedByCurrentUser;
}