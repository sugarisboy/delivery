package dev.muskrat.delivery.auth.security.jwt;

import java.util.Optional;
import java.util.Set;

public interface TokenStore {

    void saveToken(Long userId, JwtToken token);

    Set<JwtToken> findTokensByUserId(Long userId);

    Optional<JwtToken> readTokenByKey(Long userId, String key);

    void removeTokenByKey(Long userId, String key);

    void clearTokensByUserId(Long userId);

    void clearExceptByKey(Long userId, String key);

    boolean containsKey(Long userId, String key);

    //JwtToken updateToken(Long userId, String key, String access);
}