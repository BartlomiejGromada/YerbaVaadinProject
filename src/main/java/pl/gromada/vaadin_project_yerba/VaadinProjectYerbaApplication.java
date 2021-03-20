package pl.gromada.vaadin_project_yerba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

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
