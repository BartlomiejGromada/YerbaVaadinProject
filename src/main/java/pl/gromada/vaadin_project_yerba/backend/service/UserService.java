package pl.gromada.vaadin_project_yerba.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserRole;
import pl.gromada.vaadin_project_yerba.backend.repo.UserRepository;
import pl.gromada.vaadin_project_yerba.backend.repo.UserRoleRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private static final String DEFAULT_ROLE = "ROLE_USER";

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void addUserWithDefaultRole(User user) {
        UserRole userRole = userRoleRepository.findByRole(DEFAULT_ROLE);
        user.getUserRoles().add(userRole);
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        userRepository.save(user);
    }

    @PostConstruct
    public void addUser() {
        userRoleRepository.save(new UserRole("ROLE_USER", "Default role for user"));
        userRoleRepository.save(new UserRole("ROLE_ADMIN", "All the privileges"));

        Set<UserRole> roles = new HashSet<>();
        UserRole userRoleAdmin = userRoleRepository.findByRole("ROLE_ADMIN");
        UserRole userRoleUser = userRoleRepository.findByRole("ROLE_USER");
        roles.add(userRoleAdmin);
        roles.add(userRoleUser);
        userRepository.save(new User("Bartas", "{noop}pass123", "Bartek", "Gromada",
               roles));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
