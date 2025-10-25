package sv.edu.udb.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                // Deshabilitar CSRF (común en APIs stateless con JWT)
                .csrf(csrf -> csrf.disable())
                // Configurar headers (necesario si usas H2 console, etc.)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // Habilitar CORS usando la configuración definida en el Bean 'corsConfigurationSource'
                .cors(Customizer.withDefaults())
                // Definir reglas de autorización para las peticiones HTTP
                .authorizeHttpRequests(authz -> authz
                        // **Permitir SIEMPRE peticiones OPTIONS (CORS Preflight) ANTES de otras reglas**
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints Públicos (sin autenticación requerida)
                        .requestMatchers("/auth/**").permitAll() // Login
                        .requestMatchers(HttpMethod.GET, "/files/**").permitAll() // Ver imágenes subidas
                        .requestMatchers(HttpMethod.GET, "/api/hello").permitAll() // Endpoint de prueba
                        .requestMatchers(HttpMethod.GET, "/api/alcancias/**").permitAll() // Ver alcancías y detalles
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/**").permitAll() // Ver proyectos y detalles
                        .requestMatchers(HttpMethod.GET, "/api/perros/**").permitAll() // Ver perros en adopción y detalles
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Registro de nuevos usuarios

                        // Endpoints Protegidos (requieren autenticación y roles específicos)

                        // Subida de archivos (solo ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/files").hasRole("ADMIN")

                        // Donaciones (crear para USER/ADMIN, ver solo ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/donaciones").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/donaciones/**").hasRole("ADMIN")

                        // Gestión de Usuarios (solo ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN") // Incluye cambio de contraseña
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMIN") // Listar/ver usuarios

                        // Gestión de Alcancías (solo ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/alcancias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/alcancias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/alcancias/**").hasRole("ADMIN")

                        // Gestión de Proyectos (solo ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/proyectos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasRole("ADMIN")

                        // Gestión de Perros (solo ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/perros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/perros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/perros/**").hasRole("ADMIN")

                        // Cualquier otra petición no definida explícitamente requiere autenticación
                        .anyRequest().authenticated()
                )
                // Deshabilitar autenticación HTTP Basic y Form Login (usaremos solo JWT)
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable())
                // Configurar manejo de sesión como STATELESS (sin sesiones en el servidor, depende del JWT)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Añadir nuestro filtro JWT antes del filtro estándar de autenticación por usuario/contraseña
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define el codificador de contraseñas que se usará en la aplicación.
     * BCrypt es el estándar recomendado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de CORS para permitir peticiones desde el frontend (Vite en localhost:5173).
     * Permite cualquier método y header desde esos orígenes y habilita credenciales (cookies/tokens).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Orígenes permitidos (tu frontend)
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // Headers permitidos (usar "*" permite todos, incluido Authorization)
        config.setAllowedHeaders(List.of("*"));
        // Permitir envío de credenciales (importante para JWT en headers o cookies)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar esta configuración a todas las rutas ("/**")
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Expone el AuthenticationManager como un Bean para poder usarlo en el AuthController.
     * Necesario para procesar las peticiones de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
