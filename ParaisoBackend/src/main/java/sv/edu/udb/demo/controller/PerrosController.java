package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.PerroCreateDTO;
import sv.edu.udb.demo.dto.PerroUpdateDTO;
import sv.edu.udb.demo.model.PerroEnAdopcion;
import sv.edu.udb.demo.service.PerroEnAdopcionService;

import java.util.List;

@RestController
@RequestMapping("/api/perros")
@RequiredArgsConstructor
public class PerrosController {

    private final PerroEnAdopcionService service;

    // GET /api/perros  (público)
    @GetMapping
    public List<PerroEnAdopcion> listar() { return service.findAll(); }

    // GET /api/perros/{id}  (público)
    @GetMapping("/{id}")
    public ResponseEntity<PerroEnAdopcion> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // POST /api/perros (admin)
    @PostMapping
    public ResponseEntity<PerroEnAdopcion> crear(@Valid @RequestBody PerroCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // PUT /api/perros/{id} (admin)
    @PutMapping("/{id}")
    public ResponseEntity<PerroEnAdopcion> actualizar(@PathVariable Integer id,
                                                      @Valid @RequestBody PerroUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // DELETE /api/perros/{id} (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
