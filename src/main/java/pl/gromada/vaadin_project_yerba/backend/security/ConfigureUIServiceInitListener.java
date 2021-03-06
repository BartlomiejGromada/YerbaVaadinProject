package pl.gromada.vaadin_project_yerba.backend.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import pl.gromada.vaadin_project_yerba.ui.views.login.LoginView;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {


    //Listen for the initialization of the UI (the internal root component in Vaadin)
    // and then add a listener before every view transition.
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    //In authenticateNavigation, we reroute all requests to the login, if the user is not logged in
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) { //
            if (SecurityUtils.isUserLoggedIn()) { //
                event.rerouteToError(NotFoundException.class); //
            } else {
                event.rerouteTo(LoginView.class); //
            }
        }
    }
}