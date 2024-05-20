package kg.neobis.cookscorner.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "saved_recipes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavedRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saved_recipe_id_seq")
    @SequenceGenerator(name = "saved_recipe_id_seq", sequenceName = "saved_recipe_id_seq", allocationSize = 1)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    Recipe recipe;
}