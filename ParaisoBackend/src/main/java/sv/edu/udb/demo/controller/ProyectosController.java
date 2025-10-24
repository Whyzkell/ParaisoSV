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
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectosController {

    private final ProyectosService service;

    // públicos
    @GetMapping
    public List<Proyectos> listar(@RequestParam(required = false) String q) {
        return service.findAll().stream()
                .filter(p -> q == null ||
                        (p.getTit()!=null && p.getTit().toLowerCase().contains(q.toLowerCase())) ||
                        (p.getDescr()!=null && p.getDescr().toLowerCase().contains(q.toLowerCase())))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyectos> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // admin: img obligatoria
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ProyectosCreateDTO dto) {
        if (dto.img()==null || dto.img().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "La imagen (img) es obligatoria"));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody ProyectosUpdateDTO dto) {
        if (dto.img()!=null && dto.img().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "La imagen (img) no puede ser vacía"));
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
