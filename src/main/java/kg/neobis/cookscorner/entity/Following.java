package kg.neobis.cookscorner.entity;

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
@Table(name = "followings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "following_id_seq")
    @SequenceGenerator(name = "following_id_seq", sequenceName = "following_id_seq", allocationSize = 1)
    Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    User follower;

    @ManyToOne
    @JoinColumn(name = "chef_id", nullable = false)
    User chef;
}