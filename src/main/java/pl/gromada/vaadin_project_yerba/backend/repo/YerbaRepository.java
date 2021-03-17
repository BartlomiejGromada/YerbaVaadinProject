package pl.gromada.vaadin_project_yerba.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;

@Repository
public interface YerbaRepository extends JpaRepository<Yerba, Long> {
}
