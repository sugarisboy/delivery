package dev.muskrat.delivery.auth.security.jwt;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"key"})
public class JwtToken {

    private String key;
    private String access;
    private String refresh;
}
