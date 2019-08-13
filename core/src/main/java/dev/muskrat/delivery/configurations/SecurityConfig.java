package dev.muskrat.delivery.configurations;

import dev.muskrat.delivery.auth.security.JwtUserDetailsService;
import dev.muskrat.delivery.auth.security.jwt.JwtBasicAuthenticationFilter;
import dev.muskrat.delivery.auth.security.jwt.JwtConfigurer;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenFilter;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String LOGIN_ENDPOINT = "/**";
    private static final String PARTNER_ENDPOINT = "";
    private static final String ADMIN_ENDPOINT = "/admin/**";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()

            .antMatchers(LOGIN_ENDPOINT).permitAll()
            .antMatchers().hasRole("PARTNER")
            .antMatchers().hasRole(ADMIN_ENDPOINT)
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilter(new JwtBasicAuthenticationFilter(authenticationManager()))
            .apply(new JwtConfigurer(jwtTokenProvider));

        http
            .formLogin()
            .loginPage("/auth/login")
            .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationProvider(jwtTokenProvider)
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoder);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/**");
    }
}