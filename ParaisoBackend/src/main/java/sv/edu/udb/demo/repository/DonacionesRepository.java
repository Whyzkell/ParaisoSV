package sv.edu.udb.demo.repository;

import sv.edu.udb.demo.model.Donaciones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonacionesRepository extends JpaRepository<Donaciones, Integer> {
}
