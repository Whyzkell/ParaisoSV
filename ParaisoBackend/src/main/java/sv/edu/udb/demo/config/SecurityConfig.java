package sv.edu.udb.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sv.edu.udb.demo.security.JwtAuthFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hello").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/alcancias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/perros/**").permitAll()

                        // Endpoints protegidos por rol
                        .requestMatchers(HttpMethod.POST, "/api/donaciones").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/donaciones/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/alcancias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/alcancias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/alcancias/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/proyectos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/perros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/perros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/perros/**").hasRole("ADMIN")

                        // Cualquier otro requiere autenticación
                        .anyRequest().authenticated()
                )
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS: solo necesario para llamadas desde navegador (Postman lo ignora).
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
