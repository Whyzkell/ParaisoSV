package sv.edu.udb.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        // Preflight CORS (navegadores)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ---- Públicos (lectura) ----
                        .requestMatchers(HttpMethod.GET, "/api/hello").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/alcancias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/perros/**").permitAll()

                        // ---- Donaciones ----
                        .requestMatchers(HttpMethod.POST, "/api/donaciones").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/api/donaciones/**").hasRole("ADMIN")

                        // ---- Usuarios (gestión) ----
                        .requestMatchers(HttpMethod.POST,   "/api/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/usuarios/**").hasRole("ADMIN") // si quieres que listar sea solo admin

                        // ---- Alcancías (gestión) ----
                        .requestMatchers(HttpMethod.POST,   "/api/alcancias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/alcancias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/alcancias/**").hasRole("ADMIN")

                        // ---- Proyectos (gestión) ----
                        .requestMatchers(HttpMethod.POST,   "/api/proyectos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/proyectos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasRole("ADMIN")

                        // ---- Perros (gestión) ----
                        .requestMatchers(HttpMethod.POST,   "/api/perros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/perros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/perros/**").hasRole("ADMIN")

                        // Todo lo demás autenticado
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
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
}
