package kg.neobis.cookscorner.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "liked_recipes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikedRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "liked_recipes_id_seq")
    @SequenceGenerator(name = "liked_recipes_id_seq", sequenceName = "liked_recipes_id_seq", allocationSize = 1)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    Recipe recipe;
}