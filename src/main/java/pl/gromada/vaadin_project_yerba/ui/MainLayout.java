package pl.gromada.vaadin_project_yerba.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import pl.gromada.vaadin_project_yerba.backend.security.SecurityUtils;
import pl.gromada.vaadin_project_yerba.ui.views.homepage.HomeView;
import pl.gromada.vaadin_project_yerba.ui.views.user_yerba_list.UserYerbaListView;
import pl.gromada.vaadin_project_yerba.ui.views.yerba_list.YerbaListView;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("World of Yerba");
        logo.addClassName("logo");

        //logout
        Button buttonLogout = new Button("Log out");
        buttonLogout.setId("logout");
        buttonLogout.addClickListener(click -> UI.getCurrent().getPage().setLocation("/logout"));

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, buttonLogout);
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    public void createDrawer() {

        RouterLink homePageLink = new RouterLink("Homepage", HomeView.class);
        homePageLink.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(new VerticalLayout(homePageLink));

        RouterLink listYerbaLink = new RouterLink("List", YerbaListView.class);
        listYerbaLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink listMyYerbaLink = new RouterLink("My Yerba", UserYerbaListView.class);
        listMyYerbaLink.setHighlightCondition(HighlightConditions.sameLocation());

        if(SecurityUtils.getUserRoles().contains("ROLE_ADMIN"))
            addToDrawer(new VerticalLayout(listYerbaLink));

        if(SecurityUtils.getUserRoles().contains("ROLE_USER"))
           addToDrawer(new VerticalLayout(listMyYerbaLink));

        setId("drawer");
    }
}
