package kg.neobis.cookscorner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(name = "is_saved", nullable = false)
    Boolean isSaved = true;
}