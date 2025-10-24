package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.repository.UsuarioRepository;
import sv.edu.udb.demo.service.DonacionesService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
public class DonacionesController {

    private final DonacionesService service;
    private final UsuarioRepository usuarioRepo;
    private final DonacionesRepository donacionesRepo;

    // Con JWT: el principal trae el username (correo)
    @PostMapping
    public Donaciones crear(@AuthenticationPrincipal(expression = "username") String username,
                            @Valid @RequestBody DonacionesCreateDTO dto) {
        Usuario u = usuarioRepo.findByCorreo(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario app no encontrado"));
        return service.crearDonacion(u.getId(), dto);
    }

    // Reporte con filtros (ADMIN)
    @GetMapping
    public List<Donaciones> listar(
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) Integer alcanciaId,
            @RequestParam(required = false) BigDecimal montoMin,
            @RequestParam(required = false) BigDecimal montoMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        var all = donacionesRepo.findAll();
        LocalDateTime from = (desde == null) ? null : desde.atStartOfDay();
        LocalDateTime to   = (hasta == null) ? null : hasta.plusDays(1).atStartOfDay();

        return all.stream()
                .filter(d -> usuarioId == null || (d.getUsuario()!=null && usuarioId.equals(d.getUsuario().getId())))
                .filter(d -> alcanciaId == null || (d.getAlcancia()!=null && alcanciaId.equals(d.getAlcancia().getId())))
                .filter(d -> montoMin == null || (d.getCantidadDonada()!=null && d.getCantidadDonada().compareTo(montoMin) >= 0))
                .filter(d -> montoMax == null || (d.getCantidadDonada()!=null && d.getCantidadDonada().compareTo(montoMax) <= 0))
                .filter(d -> from == null || (d.getFecha()!=null && !d.getFecha().isBefore(from)))
                .filter(d -> to == null || (d.getFecha()!=null && d.getFecha().isBefore(to)))
                .toList();
    }
}
