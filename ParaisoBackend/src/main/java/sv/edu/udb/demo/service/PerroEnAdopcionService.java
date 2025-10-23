package sv.edu.udb.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.dto.PerroCreateDTO;
import sv.edu.udb.demo.dto.PerroUpdateDTO;
import sv.edu.udb.demo.model.PerroEnAdopcion;
import sv.edu.udb.demo.repository.PerroEnAdopcionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerroEnAdopcionService {

    private final PerroEnAdopcionRepository repo;

    // Lecturas
    public List<PerroEnAdopcion> findAll() { return repo.findAll(); }

    public PerroEnAdopcion findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Perro no existe"));
    }

    // Crear
    @Transactional
    public PerroEnAdopcion create(PerroCreateDTO dto) {
        PerroEnAdopcion p = PerroEnAdopcion.builder()
                .nombre(dto.nombre())
                .raza(dto.raza())
                .edad(dto.edad())
                .img(dto.img())
                .descr(dto.descr())
                .build();
        return repo.save(p);
    }

    // Actualizar parcial
    @Transactional
    public PerroEnAdopcion update(Integer id, PerroUpdateDTO dto) {
        PerroEnAdopcion db = findById(id);
        if (dto.nombre() != null) db.setNombre(dto.nombre());
        if (dto.raza()   != null) db.setRaza(dto.raza());
        if (dto.edad()   != null) db.setEdad(dto.edad());
        if (dto.img()    != null) db.setImg(dto.img());
        if (dto.descr()  != null) db.setDescr(dto.descr());
        return repo.save(db);
    }

    // Eliminar
    @Transactional
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Perro no existe");
        repo.deleteById(id);
    }
}
