package sv.edu.udb.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.AlcanciaRepository;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class DonacionesService {

    private final DonacionesRepository donacionesRepo;
    private final AlcanciaRepository alcanciaRepo;
    private final UsuarioRepository usuarioRepo;

    @Transactional
    public Donaciones crearDonacion(Integer idUsuario, DonacionesCreateDTO dto) {
        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
        Alcancia alc = alcanciaRepo.findById(dto.idAlcancia())
                .orElseThrow(() -> new IllegalArgumentException("Alcancía no existe"));

        BigDecimal monto = dto.cantidad().setScale(2, RoundingMode.HALF_UP);
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // 1) crear donación
        Donaciones don = Donaciones.builder()
                .usuario(user)
                .alcancia(alc)
                .cantidadDonada(monto)
                .build();
        donacionesRepo.save(don);

        // 2) actualizar total de la alcancía
        alc.setPrecioActual(alc.getPrecioActual().add(monto).setScale(2, RoundingMode.HALF_UP));
        alcanciaRepo.save(alc);

        return don;
    }
}
