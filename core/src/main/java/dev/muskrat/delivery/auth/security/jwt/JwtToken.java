package dev.muskrat.delivery.auth.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"key"})
public class JwtToken {

    private String key;
    private String access;
    private String refresh;
}
