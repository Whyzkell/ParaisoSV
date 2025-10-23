package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.AlcanciaCreateDTO;
import sv.edu.udb.demo.dto.AlcanciaUpdateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.service.AlcanciaService;

import java.util.List;

@RestController
@RequestMapping("/api/alcancias")
@RequiredArgsConstructor
public class AlcanciaController {

    private final AlcanciaService service;

    // GET /api/alcancias  (público)
    @GetMapping
    public List<Alcancia> listar() { return service.findAll(); }

    // GET /api/alcancias/{id}  (público)
    @GetMapping("/{id}")
    public ResponseEntity<Alcancia> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // POST /api/alcancias  (admin)
    @PostMapping
    public ResponseEntity<Alcancia> crear(@Valid @RequestBody AlcanciaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // PUT /api/alcancias/{id}  (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Alcancia> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody AlcanciaUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // DELETE /api/alcancias/{id}  (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
