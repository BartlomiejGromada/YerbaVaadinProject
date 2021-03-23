package pl.gromada.vaadin_project_yerba.ui.views.yerba_list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import pl.gromada.vaadin_project_yerba.backend.enums.Brand;
import pl.gromada.vaadin_project_yerba.backend.enums.Country;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;
import pl.gromada.vaadin_project_yerba.backend.service.YerbaService;
import pl.gromada.vaadin_project_yerba.ui.MainLayout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Route(value = "yerba", layout = MainLayout.class)
@PageTitle("Yerba")
@Secured("ROLE_ADMIN")
public class YerbaListView extends VerticalLayout {

    Grid<Yerba> yerbaGrid = new Grid<>(Yerba.class);
    TextField filterNameText = new TextField();
    Select<String> filterBrandSelect = new Select<>();
    Select<String> filterCountrySelect= new Select<>();
    Icon filterIcon = new Icon(VaadinIcon.FILTER);
    private YerbaForm yerbaForm;

    private YerbaService yerbaService;

    public YerbaListView(YerbaService yerbaService) {
        this.yerbaService = yerbaService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        yerbaForm = new YerbaForm();
        yerbaForm.addListener(YerbaForm.SaveEvent.class, this::saveYerba);
        yerbaForm.addListener(YerbaForm.DeleteEvent.class, this::deleteYerba);
        yerbaForm.addListener(YerbaForm.CloseEvent.class, evt -> closeEditor());

        Div content = new Div(yerbaGrid, yerbaForm);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList(null, null, null);
        closeEditor();

    }

    private void saveYerba(YerbaForm.SaveEvent evt) {
        yerbaService.saveYerba(evt.getYerba());
        updateList(filterNameText.getValue(), filterBrandSelect.getValue(), filterCountrySelect.getValue());
        closeEditor();
    }

    private void deleteYerba(YerbaForm.DeleteEvent evt) {
        yerbaService.deleteYerba(evt.getYerba());
        updateList(filterNameText.getValue(), filterBrandSelect.getValue(), filterCountrySelect.getValue());
        closeEditor();

    }

    private void closeEditor() {
        yerbaForm.setYerba(null);
        yerbaForm.setVisible(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        yerbaGrid.addClassName("grid");
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

        yerbaGrid.asSingleSelect().addValueChangeListener(evt -> editYerba(evt.getValue()));
    }

    private void editYerba(Yerba yerba) {
        if(yerba == null) {
            closeEditor();
        } else {
            yerbaForm.setYerba(yerba);
            yerbaForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void configureFilter() {
        //icon filter settings
        filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.getStyle().set("cursor", "pointer");
        filterIcon.setSize("35px");
        AtomicBoolean visible = new AtomicBoolean(false);
        filterIcon.addClickListener(c -> {
            if (!visible.get()) {
                filterNameText.setVisible(true);
                filterBrandSelect.setVisible(true);
                filterCountrySelect.setVisible(true);
                visible.set(true);
            } else {
                filterNameText.setVisible(false);
                filterBrandSelect.setVisible(false);
                filterCountrySelect.setVisible(false);
                visible.set(false);
            }
        });

        //filter by name settings
        filterNameText.setPlaceholder("Name");
        filterNameText.setVisible(false);
        filterNameText.setClearButtonVisible(true);
        filterNameText.setValueChangeMode(ValueChangeMode.LAZY);
        filterNameText.addValueChangeListener(t -> {
            updateList(t.getValue(), filterBrandSelect.getValue(), filterCountrySelect.getValue());
        });

        //filter by brand settings
        List<String> brands = Arrays.stream((Brand.values())).map(Brand::name).collect(Collectors.toList());
        setSelectFilter(filterBrandSelect, brands, "Brand");

        //filter by country
        List<String> countries = Arrays.stream((Country.values())).map(Country::name).collect(Collectors.toList());
        setSelectFilter(filterCountrySelect, countries, "Country");
    }

    private HorizontalLayout getToolbar() {
        configureFilter();

        Button buttonAddYerba = new Button("Add yerba");
        buttonAddYerba.addClickListener(click -> addYerba());
        buttonAddYerba.setId("button-add");

        //add filterLayout to MainLayout
        HorizontalLayout toolbar = new HorizontalLayout(filterIcon, filterNameText, filterBrandSelect,
                filterCountrySelect, buttonAddYerba);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void addYerba() {
        yerbaGrid.asSingleSelect().clear();
        editYerba(new Yerba());
    }

    private void setSelectFilter(Select<String> filterSelect, List<String> values, String placeholder) {
        values.add(0, "");
        filterSelect.setItems(values);
        filterSelect.setValue(values.get(0));
        filterSelect.setPlaceholder(placeholder);
        filterSelect.setVisible(false);
        filterSelect.addValueChangeListener(s -> {
            updateList(filterNameText.getValue(), filterBrandSelect.getValue(), filterCountrySelect.getValue());
        });
    }

    private void updateList(String nameFilter, String brandFilter, String countryFilter) {
        yerbaGrid.setItems(yerbaService.findAllYerba(nameFilter, brandFilter, countryFilter));
    }
}
