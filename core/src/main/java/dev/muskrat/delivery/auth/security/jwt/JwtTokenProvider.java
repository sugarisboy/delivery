package dev.muskrat.delivery.auth.security.jwt;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.components.exception.JwtAuthenticationException;
import dev.muskrat.delivery.components.exception.JwtTokenExpiredException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements AuthenticationProvider {

    private final TokenStore tokenStore;
    private final UserDetailsService userDetailsService;
    private final JwtPasswordEncoder jwtPasswordEncoder;

    @Value("${application.jwt.token.secret}")
    private String secret;

    @Value("${application.jwt.token.expired}")
    private long expiredTime;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public JwtToken generateJwtToken(User user) {
        return generateJwtToken(user, new JwtToken());
    }

    public JwtToken updateJwtToken(User user, String refresh) {
        Long userId = user.getId();

        Set<JwtToken> tokens = tokenStore.findTokensByUserId(userId);

        JwtToken token = tokens.stream()
            .filter(t -> t.getRefresh().equals(refresh))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Refresh token is not valid"));

        return generateJwtToken(user, token);
    }

    public Long getId(String access) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(access)
            .getBody()
            .get("id", Long.class);
    }

    public String resolveToken(HttpServletRequest req) {
        return resolveToken(req.getHeader("Authorization"));
    }

    public String resolveToken(String authorization) {
        String bearerToken = authorization;
        if (bearerToken != null && bearerToken.startsWith("Bearer_"))
            return bearerToken.substring(7);
        return null;
    }

    public String getUsername(String access) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(access)
            .getBody()
            .getSubject();
    }

    // UGLY CODE
    public boolean validateAccessToken(String key, String access) {
        try {

            Jws<Claims> claims;

            try {
                claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(access);
            } catch (ExpiredJwtException e) {
                throw new JwtTokenExpiredException("Token is expired");
            }

            Long userId = getId(access);
            Claims body = claims.getBody();
            Date expiration = body.getExpiration();

            return expiration != null &&
                !claims.getBody().getExpiration().before(new Date()) &&
                tokenStore.containsKey(userId, key);

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    // UGLY CODE
    public boolean validateRefreshToken(String key, String refresh) {
        try {

            try {
                Jwts.parser().setSigningKey(secret).parseClaimsJws(refresh);
            } catch (ExpiredJwtException e) {
                throw new JwtTokenExpiredException("Token is expired");
            }

            Long userId = getId(refresh);
            return tokenStore.containsKey(userId, key);

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    private JwtToken generateJwtToken(User user, JwtToken token) {
        String username = user.getUsername();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", user.getId());
        claims.put("roles", getRoleNames(user.getRoles()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredTime);

        String refresh = generateRefreshToken(user);

        String access = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();

        String key = generateKey(refresh);

        token.setKey(key);
        token.setAccess(access);
        token.setRefresh(refresh);

        tokenStore.saveToken(user.getId(), token);

        return token;
    }

    private String generateKey(String refresh) {
        return jwtPasswordEncoder.passwordEncoder()
            .encode(refresh.substring(16) + "-SUPER-SALT-" + System.currentTimeMillis());
    }

    private String generateRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId());

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    private List<String> getRoleNames(List<Role> roles) {
        return roles.stream()
            .map(Role::getName)
            .collect(Collectors.toList());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return new UsernamePasswordAuthenticationToken(name, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
