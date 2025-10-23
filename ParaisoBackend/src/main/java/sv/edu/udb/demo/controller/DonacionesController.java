package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.UsuarioRepository;
import sv.edu.udb.demo.service.DonacionesService;

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
public class DonacionesController {

    private final DonacionesService service;
    private final UsuarioRepository usuarioRepo;

    @PostMapping
    public Donaciones crear(@AuthenticationPrincipal UserDetails auth,
                            @Valid @RequestBody DonacionesCreateDTO dto) {
        Usuario u = usuarioRepo.findByCorreo(auth.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario app no encontrado"));
        return service.crearDonacion(u.getId(), dto);
    }
}
