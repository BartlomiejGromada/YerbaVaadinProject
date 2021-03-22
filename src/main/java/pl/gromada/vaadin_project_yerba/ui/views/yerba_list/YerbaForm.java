package pl.gromada.vaadin_project_yerba.ui.views.yerba_list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import pl.gromada.vaadin_project_yerba.backend.enums.Brand;
import pl.gromada.vaadin_project_yerba.backend.enums.Country;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;

public class YerbaForm extends FormLayout {

    TextField name = new TextField("Name");
    ComboBox<Brand> brand = new ComboBox<>("Brand");
    ComboBox<Country> country = new ComboBox<>("Country");
    TextField photo = new TextField("Photo url");
    IntegerField stars = new IntegerField("Stars");

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button closeButton = new Button("Close");

    Binder<Yerba> binder = new BeanValidationBinder<>(Yerba.class);

    public YerbaForm() {
        addClassName("form");

        stars.setHasControls(true);
        stars.setStep(1);
        stars.setMin(1);
        stars.setMax(6);

        binder.bindInstanceFields(this);
        brand.setItems(Brand.values());
        country.setItems(Country.values());

        add(
                name,
                brand,
                country,
                photo,
                stars,
                createButtonLayout()
        );
    }

    public void setYerba(Yerba yerba) {
        binder.setBean(yerba);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(click -> validateAndSave());
        deleteButton.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        closeButton.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, deleteButton, closeButton);

    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class YerbaFormEvent extends ComponentEvent<YerbaForm> {
        private Yerba yerba;

        protected YerbaFormEvent(YerbaForm source, Yerba yerba) {
            super(source, false);
            this.yerba = yerba;
        }

        public Yerba getYerba() {
            return yerba;
        }
    }

    public static class SaveEvent extends YerbaFormEvent {
        SaveEvent(YerbaForm source, Yerba yerba) {
            super(source, yerba);
        }
    }

    public static class DeleteEvent extends YerbaFormEvent {
        DeleteEvent(YerbaForm source, Yerba yerba) {
            super(source, yerba);
        }
    }

    public static class CloseEvent extends YerbaFormEvent {
        CloseEvent(YerbaForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
