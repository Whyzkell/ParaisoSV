package sv.edu.udb.demo.repository;

import sv.edu.udb.demo.model.PerroEnAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerroEnAdopcionRepository extends JpaRepository<PerroEnAdopcion, Integer> {
}
