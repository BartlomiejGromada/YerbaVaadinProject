package pl.gromada.vaadin_project_yerba.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gromada.vaadin_project_yerba.backend.enums.Brand;
import pl.gromada.vaadin_project_yerba.backend.enums.Country;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class Yerba implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idYerba;
    @NotBlank
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Brand brand;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country country;
    @NotBlank
    private String photo;

    public Yerba(String name, Brand brand, Country country, String photo) {
        this.name = name;
        this.brand = brand;
        this.country = country;
        this.photo = photo;
    }

}
