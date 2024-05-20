package kg.neobis.cookscorner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.neobis.cookscorner.entity.Image;
import kg.neobis.cookscorner.entity.Ingredient;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.enums.Category;
import kg.neobis.cookscorner.enums.Difficulty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDto {

    Long id;

    Image image;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100)
    String name;

    @NotBlank(message = "Description is required")
    String description;

    Difficulty difficulty;

    Category category;

    @NotBlank(message = "Preparation time is required")
    String preparation_time;

    User user;

    List<Ingredient> ingredients;
}