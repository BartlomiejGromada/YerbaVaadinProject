package pl.gromada.vaadin_project_yerba.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserYerba;

import java.util.List;

@Repository
public interface UserYerbaRepository extends JpaRepository<UserYerba, Long> {
    List<UserYerba> findAllByUser(User user);
}
