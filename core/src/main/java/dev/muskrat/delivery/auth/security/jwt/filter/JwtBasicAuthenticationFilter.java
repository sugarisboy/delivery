package dev.muskrat.delivery.auth.security.jwt.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtBasicAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
}
