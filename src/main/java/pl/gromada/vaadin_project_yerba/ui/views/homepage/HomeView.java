package pl.gromada.vaadin_project_yerba.ui.views.homepage;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserRole;
import pl.gromada.vaadin_project_yerba.backend.security.SecurityUtils;
import pl.gromada.vaadin_project_yerba.backend.service.UserService;
import pl.gromada.vaadin_project_yerba.ui.MainLayout;

import java.util.stream.Collectors;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Homepage")
@Secured("ROLE_USER")
public class HomeView extends VerticalLayout {

    private UserService userService;
    private TextField username = new TextField("Username");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextArea roles = new TextArea("Roles");

    public HomeView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(    new H1("World yerba"),
                new H3("This project is example of using Vaadin by Bartłomiej Gromada")
        );
        userInfo();

    }

    private void userInfo() {
        String usernameString = SecurityUtils.getCurrentUserUsername();
        User user = userService.findUserByUsername(usernameString);
        username.setReadOnly(true);
        firstName.setReadOnly(true);
        lastName.setReadOnly(true);
        roles.setReadOnly(true);

        username.setValue(user.getUsername());
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        roles.setValue(user.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList()).toString());

        add(
                new H5("Your profile"),
                new HorizontalLayout(firstName, lastName),
                new HorizontalLayout(username, roles)
        );
    }
}
