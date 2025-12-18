package car.selling.config;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Chain 1: endpoints públicos
    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/auth/alta-empleado",
                "/auth/alta-interesado",
                "/pruebas/v3/api-docs/**",
                "/pruebas/swagger-ui/**",
                "/pruebas/swagger-ui.html",                
                "/public/**")
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }

    // Chain 2: resto de la API protegida por JWT
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("admin")
                .requestMatchers("/auth/alta-admin").hasRole("admin")
                .requestMatchers("/pruebas/distancia-recorridas-por-periodo").hasRole("admin")
                .requestMatchers("/pruebas/vehiculo").hasRole("admin")
                .requestMatchers("/pruebas").hasRole("empleado")
                .requestMatchers("/pruebas/enviar-notificacion").hasRole("empleado")
                .requestMatchers("/pruebas/en-curso").hasRole("empleado")
                .requestMatchers("/pruebas/*/finalizar").hasRole("empleado")
                .requestMatchers("/pruebas/check-posicion-vehiculo").hasRole("interesado")
                .requestMatchers("/pruebas/prueba-en-curso").hasRole("interesado")
                .requestMatchers("/incidencias/**").hasAnyRole("admin","empleado")
                .requestMatchers("/vehiculos/**").hasAnyRole("empleado","admin")
                .requestMatchers("/interesados/**").hasAnyRole("empleado","admin")
                .requestMatchers("/empleados/**").hasAnyRole("admin","empleado")
                .requestMatchers("/modelos/**").hasAnyRole("admin","empleado","interesado")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
            JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

            jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                Set<String> roles = new HashSet<>();

                // Extraer realm roles (realm_access.roles)
                Object realmAccess = jwt.getClaim("realm_access");
                if (realmAccess instanceof Map) {
                    Object r = ((Map<?, ?>) realmAccess).get("roles");
                    if (r instanceof Collection) {
                        ((Collection<?>) r).forEach(role -> roles.add(String.valueOf(role)));
                    }
                }

                // Extraer client/resource roles (resource_access.<client>.roles)
                Object resourceAccess = jwt.getClaim("resource_access");
                if (resourceAccess instanceof Map) {
                    Map<?, ?> resources = (Map<?, ?>) resourceAccess;
                    for (Object clientObj : resources.values()) {
                        if (clientObj instanceof Map) {
                            Object clientRoles = ((Map<?, ?>) clientObj).get("roles");
                            if (clientRoles instanceof Collection) {
                                ((Collection<?>) clientRoles).forEach(role -> roles.add(String.valueOf(role)));
                            }
                        }
                    }
                }

                // Normalizar y mapear a GrantedAuthority con prefijo ROLE_
                return roles.stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase) // normalizar a mayúsculas para consistencia
                    .map(r -> "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            });

            return jwtConverter;
        }
}