package com.szbk.View;

import com.szbk.Controller.*;
import com.szbk.Model.Entity.Customer;
import com.szbk.Model.Entity.LaborUser;
import com.szbk.Model.Entity.Purification;
import com.szbk.Model.Entity.User;
import com.szbk.View.customer.CustomerUI;
import com.szbk.View.laborUser.LaborUserUI;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.VaadinNavigatorConfiguration;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class LoginWindow extends Window implements View {
    private CustomerController customerController;
    private OrderController orderController;
    private LaborUserController laborUserController;
    private PurificationController purificationController;
    private TypeController typeController;

    private Binder<User> dataBinder;

    private HorizontalLayout layout;
    private VerticalLayout mainLayout;
    private TextField email;
    private PasswordField password;

    public LoginWindow(CustomerController cc, OrderController oc, LaborUserController lc, PurificationController pc, TypeController tc) {
        this.customerController = cc;
        this.orderController = oc;
        this.laborUserController = lc;
        this.purificationController = pc;
        this.typeController = tc;

        dataBinder = new Binder<>(User.class);

        //It contains the two textfields and the login button.
        layout = new HorizontalLayout();
        //This contains the horizontal layout and the registration button, which is below them.
        mainLayout = new VerticalLayout();

        setCaption("Belépés");
        setClosable(false);
        setDraggable(false);
        setResizable(false);

        setContent(layout);
        //With this the login window will be modal (you can't click out of it), and it will be in center position.
        setModal(true);

        setupContent();

        setSizeUndefined();
    }

    private void setupContent() {
        Notification notification = new Notification("Sikertelen belépés! Nincs ilyen felhasználó!");
        notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
        notification.setPosition(Position.TOP_CENTER);

        Button loginButton = new Button("Belépés");
        Button registrationButton = new Button("Regisztráció");
        email = new TextField();
        email.focus();
        dataBinder.forField(email).withValidator(new EmailValidator("Ez nem valid email cím!"))
                .bind(User::getEmail, User::setEmail);

        password = new PasswordField();
        dataBinder.forField(password).bind(User::getPassword, User::setPassword);

        //A notification which appears when I click any button (in development)
        Notification demoNotification = new Notification("");
        demoNotification.setPosition(Position.TOP_CENTER);

        //Setting up how the login button, the email and the password field will look like.
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(e -> {
            User user = new User();

            if (validateUser(user)) {
                Customer c = null;
                LaborUser lb = laborUserController.login(user);

                if (lb != null) {
                    VaadinSession.getCurrent().setAttribute("laborUserName", lb.getName());
                    VaadinSession.getCurrent().setAttribute("email", lb.getEmail());
                    VaadinSession.getCurrent().setAttribute("id", lb.getId());
                    getUI().setContent(new LaborUserUI(orderController, customerController, laborUserController, purificationController, typeController));
                    getUI().removeWindow(this);
                } else {
                    c = customerController.login(user);
                }

                if (c != null) {
                    clearFields();
                    VaadinSession.getCurrent().setAttribute("customerName", c.getCustomerName());
                    VaadinSession.getCurrent().setAttribute("groupName", c.getGroupName());
                    VaadinSession.getCurrent().setAttribute("companyName", c.getCompanyName());
                    VaadinSession.getCurrent().setAttribute("id", c.getId());
                    VaadinSession.getCurrent().setAttribute("innerName", c.getInnerName());
                    getUI().setContent(new CustomerUI(customerController, orderController, laborUserController, purificationController, typeController));
                    getUI().removeWindow(this);
                //Some action, like the validateCustomer method, to validate a non-customer user (so a laborUser)
                } else if (lb == null) {
                    notification.show(getUI().getPage());
                }
            }
        });

        registrationButton.addClickListener(e -> {
            clearFields();
            getUI().addWindow(new RegistrationWindow(customerController));
        });

        email.setPlaceholder("Email cím");
        email.setIcon(VaadinIcons.USER);
        email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password.setPlaceholder("Jelszó");
        password.setIcon(VaadinIcons.LOCK);
        password.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        layout.addComponents(email, password, loginButton);
        mainLayout.addComponents(layout, registrationButton);
        mainLayout.setComponentAlignment(registrationButton, Alignment.BOTTOM_CENTER);
        setContent(mainLayout);
    }

    private boolean validateUser(User user) {
        try {
            dataBinder.writeBean(user);
            System.out.println("user: " + user);
            return true;
        } catch(ValidationException e) {
            Notification notification = new Notification("Ellenőrizze a hibaüzeneteket az egyes mezők mellett!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
            notification.show(getUI().getPage());
        }

        return false;
    }

    private void clearFields() {
        email.clear();
        password.clear();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}

/*
public class LoginWindow extends Window {
    VerticalLayout loginPanel;

    public LoginWindow() {
        setSizeFull();
        buildLoginForm();
        addComponent(loginPanel);
        setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }

    private void buildLoginForm() {
        loginPanel = new VerticalLayout();
        HorizontalLayout elements = new HorizontalLayout();
        TextField email = new TextField();
        TextField password = new TextField();
        Button loginButton = new Button("Belépés");
        Button registrationButton = new Button("Regisztráció");

        email.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        email.setIcon(VaadinIcons.USER);
        email.setPlaceholder("Email cím");

        password.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.setIcon(VaadinIcons.LOCK);
        password.setPlaceholder("Jelszó");

        loginButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.addClickListener(e -> {
            Notification.show("Login button was clicked");
            getUI().setContent(new CustomerUI());
        });

        registrationButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        registrationButton.addClickListener(e -> {
            Notification.show("Registration button was clicked");
        });

        elements.setSpacing(true);
        elements.addComponents(email, password, loginButton);

        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        loginPanel.addComponents(elements, registrationButton);
        loginPanel.setComponentAlignment(registrationButton, Alignment.BOTTOM_CENTER);
    }
}
*/
