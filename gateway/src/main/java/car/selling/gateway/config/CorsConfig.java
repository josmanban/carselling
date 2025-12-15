package car.selling.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Value("${spring.web.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Value("${spring.web.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${spring.web.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${spring.web.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${spring.web.cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Allow origin patterns so '*' can be used when credentials are false.
        if (allowedOrigins != null && !allowedOrigins.isBlank()) {
            // split by comma and trim
            String[] origins = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim).toArray(String[]::new);
            // If exactly '*' then use allowedOriginPatterns for flexibility
            if (origins.length == 1 && "*".equals(origins[0])) {
                corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
            } else {
                corsConfig.setAllowedOrigins(Arrays.asList(origins));
            }
        }

        // Methods
        corsConfig.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));

        // Headers
        corsConfig.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));

        corsConfig.setAllowCredentials(allowCredentials);
        corsConfig.setMaxAge(maxAge);

        // Expose common auth headers
        corsConfig.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Location"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }
}
