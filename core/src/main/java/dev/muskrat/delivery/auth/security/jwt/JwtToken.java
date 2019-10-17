package dev.muskrat.delivery.auth.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    private String key;
    private String access;
    private String refresh;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtToken token = (JwtToken) o;
        return key.equals(token.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
