package com.szbk;

import com.szbk.Controller.*;
import com.szbk.Model.Entity.*;
import com.szbk.View.LoginWindow;
import com.vaadin.annotations.Title;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * Created by dante on 2017.04.04..
 */

@Title("Orderhandler")
@SpringUI
public class OrderhandlerUI extends UI {
    private LoginWindow loginWindow;
    private VerticalLayout welcomeScreen;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private LaborUserController laborUserController;

    @Autowired
    private PurificationController purificationController;

    @Autowired
    private TypeController typeController;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Navigator navigator = new Navigator(this, this);
        //navigator.addView("customerUI", CustomerUI.class);
        loginWindow = new LoginWindow(customerController, orderController, laborUserController, purificationController, typeController);
        setupWelcomeScreen();
        addWindow(loginWindow);

        //Puts some demo data to the database.
        demoData();

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

    private void demoData() {
        laborUserController.registration(new LaborUser("admin", "admin@vaadin.com", "admin"));
        customerController.registration(new Customer("aaa", "aaa", "teszt.aaa@b.com", "aaa", "aaa"));
        CustomerOrder a = new CustomerOrder("aaa", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now());
        CustomerOrder b = new CustomerOrder("aaa", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now());
        CustomerOrder c = new CustomerOrder("aaa", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now());

        a.setStatus("Elkészült");
        b.setStatus("Elkészült");
        c.setStatus("Elkészült");
        orderController.saveManyOrder(
                a, b, c,
                new CustomerOrder("aaa", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("aaa", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("bbb", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("bbb", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("bbb", "bbb", "bbb", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("bbb", "bbb", "bbb", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("bbb", "bbb", "bbb", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ccc", "bbb", "bbb", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ccc", "ccc", "ccc", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ccc", "ccc", "ccc", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ccc", "ccc", "ccc", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ccc", "ccc", "ccc", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ddd", "aaa", "ccc", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ddd", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ddd", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ddd", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now()),
                new CustomerOrder("ddd", "aaa", "aaa", "asdasa12312", 1.23, "alapos", "DNS", LocalDate.now())
        );

        purificationController.savePurification(new Purification("Egyszerű", 2000));
        purificationController.savePurification(new Purification("Összetett", 3000));
        typeController.saveType(new Type("RNS", 2000));
        typeController.saveType(new Type("DNS", 3000));
        typeController.saveType(new Type("Módosított", 5000));
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
