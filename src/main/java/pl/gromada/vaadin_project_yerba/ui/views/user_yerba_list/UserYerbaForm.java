package pl.gromada.vaadin_project_yerba.ui.views.user_yerba_list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import pl.gromada.vaadin_project_yerba.backend.enums.Brand;
import pl.gromada.vaadin_project_yerba.backend.enums.Country;
import pl.gromada.vaadin_project_yerba.backend.model.User;
import pl.gromada.vaadin_project_yerba.backend.model.UserYerba;
import pl.gromada.vaadin_project_yerba.backend.model.Yerba;
import pl.gromada.vaadin_project_yerba.backend.security.SecurityUtils;
import pl.gromada.vaadin_project_yerba.backend.service.UserService;
import pl.gromada.vaadin_project_yerba.backend.service.YerbaService;

public class UserYerbaForm extends FormLayout {

    Icon filterIcon = new Icon(VaadinIcon.FILTER);
    boolean filterVisible = false;
    ComboBox<Brand> brand = new ComboBox<>("Brand");
    ComboBox<Country> country = new ComboBox<>("Country");
    ComboBox<Yerba> yerba = new ComboBox<>("Yerba");

    TextArea description = new TextArea("Description");
    IntegerField stars = new IntegerField("Stars");

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button closeButton = new Button("Close");

    Binder<UserYerba> binder = new BeanValidationBinder<>(UserYerba.class);

    YerbaService yerbaService;
    UserService userService;

    public UserYerbaForm(YerbaService yerbaService, UserService userService) {
        this.yerbaService = yerbaService;
        this.userService = userService;
        addClassName("form");

        filterIcon.setSize("35px");
        filterIcon.addClickListener(click -> {
            if(filterVisible) {
                brand.setVisible(false);
                country.setVisible(false);
                filterVisible = false;
            } else {
                brand.setVisible(true);
                country.setVisible(true);
                filterVisible = true;
            }
        });

        stars.setHasControls(true);
        stars.setStep(1);
        stars.setMin(1);
        stars.setMax(6);

        binder.bindInstanceFields(this);
        brand.setClearButtonVisible(true);
        brand.setVisible(false);
        brand.setItems(Brand.values());
        country.setItems(Country.values());
        country.setClearButtonVisible(true);
        country.setVisible(false);
        setComboBoxYerba();

        brand.addValueChangeListener(value -> setComboBoxYerba());
        country.addValueChangeListener(value -> setComboBoxYerba());

        add(
                filterIcon,
                brand,
                country,
                yerba,
                stars,
                description,
                createButtonLayout()
        );
    }

    public void setUserYerba(UserYerba userYerba) {
        binder.setBean(userYerba);
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
        if (binder.isValid()) {
            User user = userService.findUserByUsername(SecurityUtils.getCurrentUserUsername());
            binder.getBean().setUser(user);
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    private void setComboBoxYerba() {
        String brandString = brand.getValue() != null ? brand.getValue().name() : "";
        String countryString = country.getValue() != null ? country.getValue().name() : "";

        if (brandString.equals("") && countryString.equals(""))
            yerba.setItems(yerbaService.findAllYerba(null, null, null));
        else {
            yerba.setItems(yerbaService.findAllYerba(null, brandString, countryString));
        }

        yerba.setItemLabelGenerator(yerbaLabel -> yerbaLabel.getName() + " (" + yerbaLabel.getBrand() + ") " +
                "- "+yerbaLabel.getCountry());
    }

    // Events
    public static abstract class YerbaFormEvent extends ComponentEvent<UserYerbaForm> {
        private UserYerba userYerba;

        protected YerbaFormEvent(UserYerbaForm source, UserYerba userYerba) {
            super(source, false);
            this.userYerba = userYerba;
        }

        public UserYerba getUserYerba() {
            return userYerba;
        }
    }

    public static class SaveEvent extends YerbaFormEvent {
        SaveEvent(UserYerbaForm source, UserYerba userYerba) {
            super(source, userYerba);
        }
    }

    public static class DeleteEvent extends YerbaFormEvent {
        DeleteEvent(UserYerbaForm source, UserYerba userYerba) {
            super(source, userYerba);
        }
    }

    public static class CloseEvent extends YerbaFormEvent {
        CloseEvent(UserYerbaForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
