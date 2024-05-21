package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.Following;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    int countByChefId(Long chefId);

    int countByFollowerId(Long followerId);
}