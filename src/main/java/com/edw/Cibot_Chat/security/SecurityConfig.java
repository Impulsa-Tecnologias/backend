package com.edw.Cibot_Chat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://127.0.0.1:3000,http://127.0.0.1:5173}")
    private List<String> allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Configuración de CORS y desactivación de CSRF (necesario para APIs REST stateless)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            
            // 2. Control de acceso a las rutas (Públicas vs Privadas)
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso libre a Swagger UI
                .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // Rutas públicas: Autenticación
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // Excepción para Usuarios Finales
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/profile").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/password").authenticated()
                
                // Restricciones de Gestión
                .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "MASTER")
                
                // El resto de la API
                .anyRequest().authenticated()
            )
            
            // 3. Gestión de sesiones: Stateless
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 4. Configuración de CORS para permitir peticiones desde el Frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // URL React / Vite
        configuration.setAllowedOrigins(allowedOrigins); 
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH","DELETE", "OPTIONS"));
        
        // Cabeceras permitidas
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir el envío de credenciales
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica CORS a todos los endpoints
        return source;
    }
}