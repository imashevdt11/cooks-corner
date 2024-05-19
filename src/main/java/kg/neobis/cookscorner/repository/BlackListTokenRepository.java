package kg.neobis.cookscorner.repository;

import kg.neobis.cookscorner.entity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    boolean existsByToken(String token);
}