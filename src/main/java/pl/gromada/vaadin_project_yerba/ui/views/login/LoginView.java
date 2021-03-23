package pl.gromada.vaadin_project_yerba.ui.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import pl.gromada.vaadin_project_yerba.ui.views.register.RegisterView;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);

        Image image = new Image("https://img.icons8.com/ios/452/mate.png", "YerbaImage");
        image.setMaxWidth("10%");
        image.setMaxHeight("10%");

        add(
                image,
                new H1("World of yerba"),
                login,
                new RouterLink("Register now", RegisterView.class)
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getQueryString().equals("error")) {
            login.setError(true);
        }
    }

}
