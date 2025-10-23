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

    // GET /api/usuarios
    @GetMapping
    public List<Usuario> all() { return service.findAll(); }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> byId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<Usuario> create(@Valid @RequestBody UsuarioCreateDTO dto) {
        Usuario created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/usuarios/{id}  (actualización parcial sencilla)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id,
                                          @RequestBody UsuarioUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // PUT /api/usuarios/{id}/password
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id,
                                               @Valid @RequestBody PasswordChangeDTO body) {
        try { service.changePassword(id, body); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
