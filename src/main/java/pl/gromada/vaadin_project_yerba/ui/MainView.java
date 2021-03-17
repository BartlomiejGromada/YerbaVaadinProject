package pl.gromada.vaadin_project_yerba.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;
import pl.gromada.vaadin_project_yerba.backend.service.YerbaService;


@Route("")
public class MainView extends VerticalLayout {

    Grid<Yerba> yerbaGrid = new Grid<>(Yerba.class);
    private YerbaService yerbaService;

    public MainView(YerbaService yerbaService) {
        this.yerbaService = yerbaService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(yerbaGrid);
        updateList();
    }

    private void configureGrid() {
        yerbaGrid.addClassName("yerba-grid");
        yerbaGrid.setSizeFull();
        yerbaGrid.removeColumnByKey("photo");
        yerbaGrid.setColumns("idYerba", "name", "brand", "country");
        yerbaGrid.addComponentColumn(yerba -> {
            Image image = new Image(yerba.getPhoto(), "photo");
            image.setWidth("100px");
            image.setHeight("100px");
            return image;
        }).setHeader("Photo");

        yerbaGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        yerbaGrid.setItems(yerbaService.findAllYerba());
    }
}
