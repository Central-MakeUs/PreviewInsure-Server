package cmc15.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost:8080/");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:3000/");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:5173/");
        config.addAllowedOrigin("https://api.previewinsure.com");
        config.addAllowedOrigin("https://api.previewinsure.com/");
        config.addAllowedOrigin("https://preview-insure-web-git-dev-sehuns-projects.vercel.app");
        config.addAllowedOrigin("https://preview-insure-web-git-dev-sehuns-projects.vercel.app/");
        config.addAllowedOrigin("https://appleid.apple.com/");
        config.addAllowedOrigin("https://appleid.apple.com");

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.setAllowCredentials(true);

        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
