package dev.muskrat.delivery.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${application.storage.upload.main}")
    private String directoryMain;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/img/**")
            // For Linux need file://
            .addResourceLocations("file:/" + directoryMain)
            .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
    }
}