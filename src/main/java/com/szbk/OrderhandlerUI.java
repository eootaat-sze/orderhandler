package com.szbk;

import com.szbk.Controller.CustomerController;
import com.szbk.Controller.LaborUserController;
import com.szbk.Controller.OrderController;
import com.szbk.Model.Entity.LaborUser;
import com.szbk.View.LoginWindow;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dante on 2017.04.04..
 */

@SpringUI
public class OrderhandlerUI extends UI {
    LoginWindow loginWindow;
    VerticalLayout welcomeScreen;

    @Autowired
    CustomerController customerController;

    @Autowired
    OrderController orderController;

    @Autowired
    LaborUserController laborUserController;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Navigator navigator = new Navigator(this, this);
        //navigator.addView("customerUI", CustomerUI.class);
        loginWindow = new LoginWindow(customerController, orderController, laborUserController);
        setupWelcomeScreen();
        addWindow(loginWindow);

        laborUserController.registration(new LaborUser("dante", "test@test.com", "dante"));

        //navigator = new Navigator(this, this);
        //showLoginWindow();
//        getNavigator().navigateTo("loginUI");
    }

    public void setupWelcomeScreen() {
        welcomeScreen = new VerticalLayout();
        Label welcomeText = new Label("Üdv az oldalon!");
        welcomeText.setStyleName(ValoTheme.LABEL_H1);
        welcomeScreen.addComponent(welcomeText);
        welcomeScreen.setComponentAlignment(welcomeText, Alignment.TOP_CENTER);
        setContent(welcomeScreen);
    }

    /*private void showLoginWindow() {
        login = new LoginWindow();
        login.addCloseListener(e -> {
            Notification.show("Ablak bezárva");
            navigator.navigateTo("customerUI");
        });

        getCurrent().addWindow(login);
    }*/
}
