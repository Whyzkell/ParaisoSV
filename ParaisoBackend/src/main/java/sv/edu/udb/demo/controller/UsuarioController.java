package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.PasswordChangeDTO;
import sv.edu.udb.demo.dto.UsuarioCreateDTO;
import sv.edu.udb.demo.dto.UsuarioUpdateDTO;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    // GET (con filtros: nombre, correo, rol)
    @GetMapping
    public List<Usuario> all(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String rol
    ) {
        return service.findAll().stream()
                .filter(u -> nombre == null || (u.getNombre() != null && u.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(u -> correo == null || (u.getCorreo() != null && u.getCorreo().toLowerCase().contains(correo.toLowerCase())))
                .filter(u -> rol == null || (u.getRol() != null && u.getRol().equalsIgnoreCase(rol)))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> byId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@Valid @RequestBody UsuarioCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id,
                                          @RequestBody UsuarioUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id,
                                               @Valid @RequestBody PasswordChangeDTO body) {
        try { service.changePassword(id, body); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
