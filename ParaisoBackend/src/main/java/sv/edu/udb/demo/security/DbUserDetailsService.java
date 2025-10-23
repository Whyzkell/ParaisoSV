package sv.edu.udb.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import sv.edu.udb.demo.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repo.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe: " + username));

        var role = "ROLE_" + u.getRol().toUpperCase(); // p.ej. ADMIN/USER
        return new org.springframework.security.core.userdetails.User(
                u.getCorreo(), u.getPassword(), List.of(new SimpleGrantedAuthority(role))
        );
    }
}
