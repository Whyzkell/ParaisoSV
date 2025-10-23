package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.ProyectosCreateDTO;
import sv.edu.udb.demo.dto.ProyectosUpdateDTO;
import sv.edu.udb.demo.model.Proyectos;
import sv.edu.udb.demo.service.ProyectosService;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectosController {

    private final ProyectosService service;

    // GET (público)
    @GetMapping
    public List<Proyectos> listar() { return service.findAll(); }

    // GET/{id} (público)
    @GetMapping("/{id}")
    public ResponseEntity<Proyectos> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // POST (admin)
    @PostMapping
    public ResponseEntity<Proyectos> crear(@Valid @RequestBody ProyectosCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // PUT/{id} (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Proyectos> actualizar(@PathVariable Integer id,
                                                @Valid @RequestBody ProyectosUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // DELETE/{id} (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
