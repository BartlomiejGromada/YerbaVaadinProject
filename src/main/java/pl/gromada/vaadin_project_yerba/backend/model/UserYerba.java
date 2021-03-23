package pl.gromada.vaadin_project_yerba.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
public class UserYerba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUserYerba;
    @ManyToOne
    @JoinColumn(name = "id_yerba")
    @NotNull(message = "{pl.gromada.backend.model.UserYerba.yerba.notNull.message}")
    private Yerba yerba;
    @ManyToOne
    @JoinColumn(name = "id_user")
    @NotNull
    private User user;
    @NotNull(message = "{pl.gromada.backend.model.UserYerba.stars.notNull.message}")
    @Min(value = 0, message = "{pl.gromada.backend.model.UserYerba.stars.min.message}")
    @Max(value = 6,message = "{pl.gromada.backend.model.UserYerba.stars.max.message}")
    private Integer stars;
    private String description;
}
