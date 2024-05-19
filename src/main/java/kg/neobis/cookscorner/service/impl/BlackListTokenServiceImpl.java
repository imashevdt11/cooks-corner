package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.entity.BlackListToken;
import kg.neobis.cookscorner.repository.BlackListTokenRepository;
import kg.neobis.cookscorner.service.BlackListTokenService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlackListTokenServiceImpl implements BlackListTokenService {
    BlackListTokenRepository blacklistTokenRepository;

    @Override
    public void addTokenToBlacklist(String token) {
        BlackListToken blacklistToken = new BlackListToken();
        blacklistToken.setToken(token);
        blacklistTokenRepository.save(blacklistToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistTokenRepository.existsByToken(token);
    }
}