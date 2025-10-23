package sv.edu.udb.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.dto.PasswordChangeDTO;
import sv.edu.udb.demo.dto.UsuarioCreateDTO;
import sv.edu.udb.demo.dto.UsuarioUpdateDTO;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    // ------ Lecturas
    public List<Usuario> findAll() { return repo.findAll(); }

    public Usuario findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
    }

    // ------ Crear
    @Transactional
    public Usuario create(UsuarioCreateDTO dto) {
        repo.findByCorreo(dto.correo()).ifPresent(u -> {
            throw new IllegalArgumentException("Correo ya está en uso");
        });

        String rol = (dto.rol() == null || dto.rol().isBlank()) ? "USER" : dto.rol().toUpperCase();

        Usuario u = Usuario.builder()
                .nombre(dto.nombre())
                .correo(dto.correo())
                .password(encoder.encode(dto.password()))
                .rol(rol)
                .build();

        return repo.save(u);
    }

    // ------ Actualizar (parcial)
    @Transactional
    public Usuario update(Integer id, UsuarioUpdateDTO dto) {
        Usuario db = findById(id);

        if (dto.correo() != null && !dto.correo().equalsIgnoreCase(db.getCorreo())) {
            repo.findByCorreo(dto.correo()).ifPresent(u -> {
                throw new IllegalArgumentException("Correo ya está en uso");
            });
            db.setCorreo(dto.correo());
        }
        if (dto.nombre() != null) db.setNombre(dto.nombre());
        if (dto.rol() != null && !dto.rol().isBlank()) db.setRol(dto.rol().toUpperCase());

        return repo.save(db);
    }

    // ------ Cambiar password
    @Transactional
    public void changePassword(Integer id, PasswordChangeDTO body) {
        Usuario db = findById(id);
        db.setPassword(encoder.encode(body.password()));
        repo.save(db);
    }

    // ------ Eliminar
    @Transactional
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Usuario no existe");
        repo.deleteById(id);
    }
}
