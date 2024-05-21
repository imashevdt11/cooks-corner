package kg.neobis.cookscorner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.neobis.cookscorner.enums.Category;
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
public class RecipeDto {

    Long id;

    String imageUrl;
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100)
    String name;

    @NotBlank(message = "Description is required")
    String description;

    Difficulty difficulty;

    Category category;

    @NotBlank(message = "Preparation time is required")
    String preparation_time;

    String username;

    List<IngredientDto> ingredients;
}