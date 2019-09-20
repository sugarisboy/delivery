package dev.muskrat.delivery.auth.security.jwt;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtTokenStore implements TokenStore {

    private Map<Long, Set<JwtToken>> store;

    public JwtTokenStore() {
        store = new HashMap<>();
    }

    @Override
    public void saveToken(Long userId, JwtToken token) {
        Set<JwtToken> jwts = findTokensByUserId(userId);
        jwts.add(token);
        store.put(userId, jwts);
    }

    @Override
    public Set<JwtToken> findTokensByUserId(Long userId) {
        return store.getOrDefault(userId, new HashSet<>());
    }

    @Override
    public Optional<JwtToken> readTokenByKey(Long userId, String key) {
        Set<JwtToken> jwts = findTokensByUserId(userId);
        return jwts.stream()
            .filter(t -> t.getKey().equals(key))
            .findFirst();
    }

    @Override
    public void removeTokenByKey(Long userId, String key) {
        Set<JwtToken> jwts = findTokensByUserId(userId);
        jwts.removeIf(p -> p.getKey().equals(key));
    }

    @Override
    public void clearTokensByUserId(Long userId) {
        store.remove(userId);
    }

    @Override
    public boolean containsKey(Long userId, String key) {
        Set<JwtToken> jwts = findTokensByUserId(userId);
        return jwts.stream()
            .filter(t -> t.getKey().equals(key))
            .findAny()
            .isPresent();
    }

    @Override
    public JwtToken updateToken(Long userId, String key, String access) {
        return null;
    }
}

