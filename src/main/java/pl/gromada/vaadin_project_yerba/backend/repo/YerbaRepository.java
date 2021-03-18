package pl.gromada.vaadin_project_yerba.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;

import java.util.List;

@Repository
public interface YerbaRepository extends JpaRepository<Yerba, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM yerba WHERE LOWER(name) LIKE LOWER(CONCAT('%', :nameFilter, '%'))" +
            "AND brand LIKE CONCAT('%', :brandFilter) AND country LIKE CONCAT('%', :countryFilter)")
    List<Yerba> findAllByFilters(String nameFilter, String brandFilter, String countryFilter);
}
