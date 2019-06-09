package dev.muskrat.delivery.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // turn off all suffix pattern matching
        configurer.setUseSuffixPatternMatch(false);
        // OR
        // turn on suffix pattern matching ONLY for suffixes
        // you explicitly register using
        // configureContentNegotiation(...)
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
}