package pl.gromada.vaadin_project_yerba.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @NotBlank(message = "{pl.gromada.backend.model.User.username.notBlank.message}")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "{pl.gromada.backend.model.User.password.notBlank.message}")
    private String password;
    @NotBlank(message = "{pl.gromada.backend.model.User.firstName.notBlank.message}")
    private String firstName;
    @NotBlank(message = "{pl.gromada.backend.model.User.lastName.notBlank.message}")
    private String lastName;
    @ManyToMany
    @JoinTable(
            name = "user_user_role",
            joinColumns = {@JoinColumn(name = "idUser", referencedColumnName = "idUser")},
            inverseJoinColumns = @JoinColumn(name = "idUserRole", referencedColumnName = "idUserRole")
    )
    private Set<UserRole> userRoles = new HashSet<>();

    public User(String username, String password, String firstName, String lastName, Set<UserRole> userRoles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRoles = userRoles;
    }
}
