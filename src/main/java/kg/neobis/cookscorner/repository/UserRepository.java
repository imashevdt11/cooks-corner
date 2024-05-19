package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByEmail(String email);

    Boolean existsUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String email);
}