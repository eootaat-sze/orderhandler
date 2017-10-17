package com.szbk.View;

import com.szbk.Controller.CustomerController;
import com.szbk.Model.Entity.Customer;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.StringTokenizer;

/**
 * Created by dante on 2017.04.09..
 */

public class RegistrationWindow extends Window {
    CustomerController controller;

    private VerticalLayout mainLayout;

    private TextField customerName;
    private PasswordField password;
    private PasswordField password2;
    private TextField email;
    private TextField groupName;
    private TextField companyName;
    private Button registrationButton;
    private Binder<Customer> dataBinder;
    private static String REQUIRED_FIELD = "A mező kitöltése kötelező!";

    public RegistrationWindow(CustomerController controller) {
        this.controller = controller;
        this.dataBinder = new Binder<>(Customer.class);

        mainLayout = new VerticalLayout();
        customerName = new TextField();
        password = new PasswordField();
        password2 = new PasswordField();
        email = new TextField();
        groupName = new TextField();
        companyName = new TextField();
        registrationButton = new Button("Regisztrálok");

        setCaption("Regisztráció");
        setClosable(true);
        setModal(true);
        setResizable(false);
        setDraggable(false);
        setSizeUndefined();

        setupContent();
    }

    private void setupContent() {
        Notification notification = new Notification("Sikeres regisztráció!");
        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
        notification.setPosition(Position.TOP_CENTER);

        customerName.setPlaceholder("Megrendelő neve");
        customerName.setDescription("Adja meg a megrendelő személy nevét");
        customerName.setWidth(100, Unit.PERCENTAGE);
        customerName.focus();
        dataBinder.forField(customerName).asRequired(REQUIRED_FIELD)
                .bind(Customer::getCustomerName, Customer::setCustomerName);
        password.setPlaceholder("Jelszó");
        password.setDescription("Adja meg a jelszavát!");
        password.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(password).asRequired(REQUIRED_FIELD);
        password2.setPlaceholder("Jelszó megint");
        password2.setDescription("Adja meg a jelszavát megint!");
        password2.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(password2).asRequired(REQUIRED_FIELD).bind(Customer::getPassword, Customer::setPassword);
        email.setPlaceholder("Email address");
        email.setDescription("Adja meg az email címét!");
        email.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(email).asRequired("Email megadása kötelező!")
                .withValidator(new EmailValidator("Ez nem valid email cím!"))
                .bind(Customer::getEmail, Customer::setEmail);
        groupName.setPlaceholder("Csoportnév");
        groupName.setDescription("Adja meg a csoport nevét!");
        groupName.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(groupName).asRequired(REQUIRED_FIELD).bind(Customer::getGroupName, Customer::setGroupName);
        companyName.setPlaceholder("Cégnév");
        companyName.setDescription("Adja meg a cég nevét!");
        companyName.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(companyName).asRequired(REQUIRED_FIELD)
                .bind(Customer::getCompanyName, Customer::setCompanyName);
        registrationButton.setDescription("Erre a gombra klikkelve regisztrálhat a rendszerbe.");
        registrationButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        registrationButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        registrationButton.addClickListener(e -> {
        //TODO If the registration is successfull, and email should be sended for the registrated email address.
            Customer customer = new Customer();
            if (validateCustomer(customer)) {
                customer.setInnerName(createInnerNameForCustomer(customer));

                if (controller.registration(customer)) {
                    clearFields();
                    notification.show(getUI().getPage());
                    this.close();
                } else {
                    notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                    notification.setCaption("Sikertelen regisztráció!");
                    notification.show(getUI().getPage());
                }
            }
        });

        mainLayout.addComponents(customerName, password, password2, email, groupName, companyName, registrationButton);
        mainLayout.setComponentAlignment(registrationButton, Alignment.BOTTOM_CENTER);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setWidth(300, Unit.PIXELS);
        setContent(mainLayout);
    }

    private String createInnerNameForCustomer(Customer customer) {
        StringTokenizer st = new StringTokenizer(customer.getCustomerName(), " ");
        StringBuilder innerName = new StringBuilder();

        while (st.hasMoreElements()) {
            innerName.append(st.nextToken().charAt(0));
        }

        innerName.append(customer.getId());

        return innerName.toString();
    }

    private boolean validateCustomer(Customer customer) {
        try {
            //System.out.println("customer: " + customer);
            dataBinder.writeBean(customer);
            return true;
        } catch(ValidationException e) {
            Notification errorMessage = new Notification("Ellenőrizze a hibaüzeneteket az egyes mezők mellett!");
            errorMessage.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            errorMessage.setPosition(Position.TOP_CENTER);
            errorMessage.show(getUI().getPage());
        }

        return false;
    }

    public void clearFields() {
        this.customerName.clear();
        this.password.clear();
        this.password2.clear();
        this.email.clear();
        this.groupName.clear();
        this.companyName.clear();
    }
}
