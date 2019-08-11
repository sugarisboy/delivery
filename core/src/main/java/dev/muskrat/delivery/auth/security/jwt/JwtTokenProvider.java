package dev.muskrat.delivery.auth.security.jwt;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.service.AuthorizedUserService;
import dev.muskrat.delivery.components.exception.JwtAuthenticationException;
import dev.muskrat.delivery.components.exception.JwtTokenExpiredException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final AuthorizedUserService userService;

    @Value("${application.jwt.token.secret}")
    private String secret;

    @Value("${application.jwt.token.expired}")
    private long expiredTime;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String refreshToken(AuthorizedUser user, String oldRefresh) {
        String refresh = user.getRefresh();
        if (refresh.equals(oldRefresh)) {
            String token = createToken(user);
            userService.updateRefresh(user, token);
            return token;
        } else {
            throw new JwtAuthenticationException("Refresh token is not valid");
        }
    }

    public String createToken(AuthorizedUser user) {

        String username = user.getUsername();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(user.getRoles()));
        claims.put("refresh", createRefresh(user));

        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredTime);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    private String createRefresh(AuthorizedUser user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        String refresh = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();

        userService.updateRefresh(user, refresh);

        return refresh;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRefresh(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("refresh", String.class);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String authorization) {
        String bearerToken = authorization;
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims;

            try {
                claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            } catch (ExpiredJwtException e) {
                throw new JwtTokenExpiredException("Token is expired");
            }

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    private List<String> getRoleNames(List<Role> roles) {
        return roles.stream()
            .map(Role::getName)
            .collect(Collectors.toList());
    }
}
