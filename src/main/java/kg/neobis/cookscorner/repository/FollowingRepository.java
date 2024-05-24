package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Following;
import kg.neobis.cookscorner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    int countByChefId(Long chefId);

    int countByFollowerId(Long followerId);

    Optional<Following> findByFollowerAndChef(User follower, User chef);
}