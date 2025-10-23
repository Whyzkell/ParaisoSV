package sv.edu.udb.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.dto.AlcanciaCreateDTO;
import sv.edu.udb.demo.dto.AlcanciaUpdateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.repository.AlcanciaRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlcanciaService {

    private final AlcanciaRepository repo;

    public List<Alcancia> findAll() { return repo.findAll(); }

    public Alcancia findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Alcancía no existe"));
    }

    @Transactional
    public Alcancia create(AlcanciaCreateDTO dto) {
        Alcancia a = Alcancia.builder()
                .descr(dto.descr())
                .precioMeta(dto.precioMeta())
                .precioActual(BigDecimal.ZERO) // inicia en 0.00
                .build();
        return repo.save(a);
    }

    @Transactional
    public Alcancia update(Integer id, AlcanciaUpdateDTO dto) {
        Alcancia db = findById(id);
        if (dto.descr() != null) db.setDescr(dto.descr());
        if (dto.precioMeta() != null) db.setPrecioMeta(dto.precioMeta());
        // precioActual NO se toca aquí
        return repo.save(db);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Alcancía no existe");
        repo.deleteById(id);
    }
}
