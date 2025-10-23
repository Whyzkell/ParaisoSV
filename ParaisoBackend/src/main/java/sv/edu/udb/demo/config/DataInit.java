package sv.edu.udb.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.UsuarioRepository;

@Configuration
@RequiredArgsConstructor
public class DataInit {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seedAdmin() {
        return args -> {
            var correo = "admin@demo.com";
            if (repo.findByCorreo(correo).isEmpty()) {
                var admin = Usuario.builder()
                        .nombre("Admin")
                        .correo(correo)
                        .password(encoder.encode("admin123")) // BCrypt
                        .rol("ADMIN")
                        .build();
                repo.save(admin);
                System.out.println(">> Admin creado: " + correo + " / admin123");
            }
        };
    }
}
