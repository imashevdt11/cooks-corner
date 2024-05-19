package kg.neobis.cookscorner.service;

public interface BlackListTokenService {

    void addTokenToBlacklist(String token);

    boolean isTokenBlacklisted(String token);
}
