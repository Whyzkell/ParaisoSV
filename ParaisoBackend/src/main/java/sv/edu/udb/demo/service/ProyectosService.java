package sv.edu.udb.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.dto.ProyectosCreateDTO;
import sv.edu.udb.demo.dto.ProyectosUpdateDTO;
import sv.edu.udb.demo.model.Proyectos;
import sv.edu.udb.demo.repository.ProyectosRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectosService {

    private final ProyectosRepository repo;

    // Lecturas
    public List<Proyectos> findAll() {
        return repo.findAll();
    }

    public Proyectos findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no existe"));
    }

    // Crear
    @Transactional
    public Proyectos create(ProyectosCreateDTO dto) {
        Proyectos p = Proyectos.builder()
                .tit(dto.tit())
                .descr(dto.descr())
                .img(dto.img())
                .build();
        return repo.save(p);
    }

    // Actualizar parcial
    @Transactional
    public Proyectos update(Integer id, ProyectosUpdateDTO dto) {
        Proyectos db = findById(id);
        if (dto.tit()   != null) db.setTit(dto.tit());
        if (dto.descr() != null) db.setDescr(dto.descr());
        if (dto.img()   != null) db.setImg(dto.img());
        return repo.save(db);
    }

    // Eliminar
    @Transactional
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Proyecto no existe");
        repo.deleteById(id);
    }
}
