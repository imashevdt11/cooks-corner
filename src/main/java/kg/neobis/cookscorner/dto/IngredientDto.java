package kg.neobis.cookscorner.dto;

import kg.neobis.cookscorner.enums.Unit;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientDto {
    String name;
    String amount;
    Unit unit;
}