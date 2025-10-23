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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/alcancias")
@RequiredArgsConstructor
public class AlcanciaController {

    private final AlcanciaService service;

    // GET (con filtros: q, metaMin/metaMax, actualMin/actualMax)
    @GetMapping
    public List<Alcancia> listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal metaMin,
            @RequestParam(required = false) BigDecimal metaMax,
            @RequestParam(required = false) BigDecimal actualMin,
            @RequestParam(required = false) BigDecimal actualMax
    ) {
        return service.findAll().stream()
                .filter(a -> q == null || (a.getDescr() != null && a.getDescr().toLowerCase().contains(q.toLowerCase())))
                .filter(a -> metaMin == null || (a.getPrecioMeta() != null && a.getPrecioMeta().compareTo(metaMin) >= 0))
                .filter(a -> metaMax == null || (a.getPrecioMeta() != null && a.getPrecioMeta().compareTo(metaMax) <= 0))
                .filter(a -> actualMin == null || (a.getPrecioActual() != null && a.getPrecioActual().compareTo(actualMin) >= 0))
                .filter(a -> actualMax == null || (a.getPrecioActual() != null && a.getPrecioActual().compareTo(actualMax) <= 0))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alcancia> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping
    public ResponseEntity<Alcancia> crear(@Valid @RequestBody AlcanciaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alcancia> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody AlcanciaUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
