package pl.gromada.vaadin_project_yerba.ui.views.register;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.apache.catalina.webresources.FileResource;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.service.UserService;
import pl.gromada.vaadin_project_yerba.ui.views.login.LoginView;

@Route("register")
@PageTitle("Register")
public class RegisterView extends VerticalLayout {

    VerticalLayout formRegistration = new VerticalLayout();
    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField username = new TextField();
    PasswordField password = new PasswordField();
    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    User user = new User();
    UserService userService;

    public RegisterView(UserService userService) {
        this.userService = userService;
        addClassName("register-form");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        createForm();
        RouterLink linkLogin = new RouterLink("Login now", LoginView.class);
        Image  image = new Image("https://img.icons8.com/ios/452/mate.png", "YerbaImage");
        image.setMaxWidth("10%");
        image.setMaxHeight("10%");

        add(
                image,
                new H1("World of yerba"),
                new H2("Registration"),
                formRegistration,
                createButtonsForm(),
                linkLogin
        );
    }

    public void createForm() {
        firstName.setLabel("First Name");
        firstName.setAutofocus(true);
        firstName.setRequired(true);
        firstName.setMinWidth("300px");

        lastName.setLabel("Last Name");
        lastName.setRequired(true);
        lastName.setMinWidth("300px");

        username.setLabel("Username");
        username.setRequired(true);
        username.setMinWidth("300px");

        password.setLabel("Password");
        password.setRequired(true);
        password.setMinWidth("300px");

        //bind textField to validation
        binder.forField(firstName).bind("firstName");
        binder.forField(lastName).bind("lastName");
        binder.forField(username).bind("username");
        binder.forField(password).bind("password");

        formRegistration.setAlignItems(Alignment.CENTER);
        formRegistration.add(firstName, lastName, username, password);
    }

    private HorizontalLayout createButtonsForm() {
        Button buttonRegister = new Button("Register");
        buttonRegister.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonRegister.setMinWidth("150px");
        buttonRegister.addClickListener(click -> validAndAddUserToDatabase());

        Button buttonReset = new Button("Reset");
        buttonReset.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        buttonReset.setMinWidth("150px");
        buttonReset.addClickListener(click -> {
            firstName.clear();
            lastName.clear();
            username.clear();
            password.clear();
            binder.removeBean();
        });

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(buttonRegister, buttonReset);
        return actions;
    }

    private void validAndAddUserToDatabase() {
        user.setFirstName(firstName.getValue());
        user.setLastName(lastName.getValue());
        user.setUsername(username.getValue());
        user.setPassword(password.getValue());
        binder.setBean(user);
        if (binder.isValid()) {
            User userExists = userService.findUserByUsername(user.getUsername());
            if (userExists == null) {
                userService.addUserWithDefaultRole(user);
                UI.getCurrent().navigate("login");
                Notification.show("Registration was successful", 5000, Notification.Position.BOTTOM_CENTER);
            } else {
                username.setInvalid(true);
                username.setErrorMessage("This nickname is taken");
            }
        } else
            Notification.show("Please enter valid values", 5000, Notification.Position.BOTTOM_CENTER);
    }
}
