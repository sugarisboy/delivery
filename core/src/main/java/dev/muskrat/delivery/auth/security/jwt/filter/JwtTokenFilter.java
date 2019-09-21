package dev.muskrat.delivery.auth.security.jwt.filter;

import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String access = jwtTokenProvider.resolveToken((httpRequest));
        String key = (httpRequest).getHeader("key");

        String requestURI = httpRequest.getRequestURI();
        boolean isRefresh = requestURI.endsWith("/refresh");

        if (access != null) {
            boolean isValid = isRefresh ?
                jwtTokenProvider.validateRefreshToken(key, access) : jwtTokenProvider.validateAccessToken(key, access);

            if (isValid) {
                Authentication authentication = jwtTokenProvider.getAuthentication(access);

                if (authentication != null)
                    SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }

        chain.doFilter(request, response);
    }
}