package pl.gromada.vaadin_project_yerba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserRole;
import pl.gromada.vaadin_project_yerba.backend.repo.UserRepository;
import pl.gromada.vaadin_project_yerba.backend.repo.UserRoleRepository;

import java.util.HashSet;
import java.util.Set;

/*
    Disable Spring MVC auto configuration on the Application class, as this interferes with how
    Vaadin works and can cause strange reloading behavior.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class VaadinProjectYerbaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaadinProjectYerbaApplication.class, args);
    }

}
