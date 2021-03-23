package pl.gromada.vaadin_project_yerba.ui.views.user_yerba_list;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserYerba;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;
import pl.gromada.vaadin_project_yerba.backend.security.SecurityUtils;
import pl.gromada.vaadin_project_yerba.backend.service.UserService;
import pl.gromada.vaadin_project_yerba.backend.service.UserYerbaService;
import pl.gromada.vaadin_project_yerba.backend.service.YerbaService;
import pl.gromada.vaadin_project_yerba.ui.MainLayout;

@Route(value = "myList", layout = MainLayout.class)
@PageTitle("My list")
@Secured("ROLE_USER")
public class UserYerbaListView extends VerticalLayout {

    Grid<UserYerba> grid = new Grid<>(UserYerba.class);
    private UserYerbaForm userYerbaForm;

    private UserYerbaService userYerbaService;
    private UserService userService;

    public UserYerbaListView(UserYerbaService userYerbaService, UserService userService, YerbaService yerbaService) {
        this.userYerbaService = userYerbaService;
        this.userService = userService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        userYerbaForm =  new UserYerbaForm(yerbaService, userService);
        userYerbaForm.addListener(UserYerbaForm.SaveEvent.class, this::saveUserYerba);
        userYerbaForm.addListener(UserYerbaForm.DeleteEvent.class, this::deleteUserYerba);
        userYerbaForm.addListener(UserYerbaForm.CloseEvent.class, evt -> closeEditor());

        Div content = new Div(grid, userYerbaForm);
        content.addClassName("content");
        content.setSizeFull();

        updateList();

        add(getToolbar(), content);
        closeEditor();
    }

    private void saveUserYerba(UserYerbaForm.SaveEvent evt) {
        userYerbaService.saveUserYerba(evt.getUserYerba());
        updateList();
        closeEditor();
    }

    private void deleteUserYerba(UserYerbaForm.DeleteEvent evt) {
        userYerbaService.deleteUserYerba(evt.getUserYerba());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        userYerbaForm.setUserYerba(null);
        userYerbaForm.setVisible(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.removeColumnByKey("stars");
        grid.removeColumnByKey("yerba");
        grid.removeColumnByKey("user");
        grid.removeColumnByKey("description");
        grid.getColumnByKey("idUserYerba").setHeader("Id");

        grid.addComponentColumn(userYerba->{
            VerticalLayout verticalLayout = new VerticalLayout();
            Yerba yerba = userYerba.getYerba();
            String name = yerba.getName() + " ("+yerba.getBrand().name()+")";
            verticalLayout.add(name);
            Image image = new Image(yerba.getPhoto(), "photo");
            image.setWidth("100px");
            image.setHeight("100px");
            verticalLayout.add(image);

            return verticalLayout;
        }).setHeader("Yerba");

        grid.addComponentColumn(userYerba -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            for (int i = 0; i < userYerba.getStars(); i++)
                horizontalLayout.add(new Icon(VaadinIcon.STAR));

            return horizontalLayout;
        }).setHeader("Stars");

        grid.addColumn("description");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editUserYerba(evt.getValue()));
    }

    private void editUserYerba(UserYerba userYerba) {
        if (userYerba == null) {
            closeEditor();
        } else {
            userYerbaForm.setUserYerba(userYerba);
            userYerbaForm.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {

        Button buttonAdd = new Button("Add");
        buttonAdd.addClickListener(click -> addUserYerba());
        buttonAdd.setId("button-add");

        //add filterLayout to MainLayout
        HorizontalLayout toolbar = new HorizontalLayout(buttonAdd);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void addUserYerba() {
        grid.asSingleSelect().clear();
        editUserYerba(new UserYerba());
    }

    private void updateList() {
        User user = userService.findUserByUsername(SecurityUtils.getCurrentUserUsername());
        grid.setItems(userYerbaService.findAllUserYerba(user));
    }

}