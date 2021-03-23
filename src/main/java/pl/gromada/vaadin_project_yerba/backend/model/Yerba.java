package pl.gromada.vaadin_project_yerba.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gromada.vaadin_project_yerba.backend.enums.Brand;
import pl.gromada.vaadin_project_yerba.backend.enums.Country;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Yerba implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idYerba;
    @NotBlank(message = "{pl.gromada.backend.model.Yerba.name.notBlank.message}")
    private String name;
    @NotNull(message = "{pl.gromada.backend.model.Yerba.brand.notNull.message}")
    @Enumerated(EnumType.STRING)
    private Brand brand;
    @NotNull(message = "{pl.gromada.backend.model.Yerba.country.notNull.message}")
    @Enumerated(EnumType.STRING)
    private Country country;
    @NotBlank(message = "{pl.gromada.backend.model.Yerba.photo.notBlank.message}")
    private String photo;
    @OneToMany(mappedBy = "yerba", cascade = CascadeType.REMOVE)
    private List<UserYerba> yerba;

    public Yerba(String name, Brand brand, Country country, String photo) {
        this.name = name;
        this.brand = brand;
        this.country = country;
        this.photo = photo;
    }

}
