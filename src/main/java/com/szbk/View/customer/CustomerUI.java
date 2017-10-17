package com.szbk.View.customer;

import com.szbk.Controller.*;
import com.szbk.Model.Entity.Purification;
import com.szbk.OrderhandlerUI;
import com.szbk.View.LoginWindow;
import com.szbk.View.SideBarMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by dante on 2017.04.11..
 */
public class CustomerUI extends HorizontalSplitPanel implements View {
    private Label descriptionText;
    private VerticalLayout descriptionTextLayout;

    private CustomerController customerController;
    private OrderController orderController;
    private LaborUserController laborUserController;
    private PurificationController purificationController;
    private TypeController typeController;

    public CustomerUI(CustomerController cc, OrderController oc, LaborUserController lc, PurificationController pc, TypeController tc) {
        this.customerController = cc;
        this.orderController = oc;
        this.laborUserController = lc;
        this.purificationController = pc;
        this.typeController = tc;

        setSplitPosition(18f, Unit.PERCENTAGE);
        setLocked(true);
        descriptionTextLayout = new VerticalLayout();
        descriptionText = new Label("Főoldal<br/>" +
                "Sikeresen beléptél, gratulálok!<br/>" +
                "Megrendelőként a bal oldalon felsorolt lehetőségek állnak a rendelkezésedre.");
        descriptionText.setContentMode(ContentMode.HTML);

        descriptionTextLayout.addComponent(descriptionText);
        descriptionTextLayout.setComponentAlignment(descriptionText, Alignment.MIDDLE_CENTER);
        setFirstComponent(setupSidebar());
        setSecondComponent(descriptionTextLayout);
    }

    private SideBarMenu setupSidebar() {
        String titleText = "Hello, " + VaadinSession.getCurrent().getAttribute("customerName") + "!";

        SideBarMenu sidebar = new SideBarMenu(titleText);
        Button orderButton = new Button("Rendelek valamit");
        Button orders = new Button("Mit rendeltem eddig?");
        Button logoutButton = new Button("Kijelentkezés");

        //Setup the order button, which is used to order something.
        orderButton.setIcon(VaadinIcons.SHOP);
        orderButton.addStyleName(ValoTheme.MENU_ITEM);
        orderButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        orderButton.addClickListener(e -> {
            setSecondComponent(new OrderWindow(this));
        });

        //Setup the orders button, which will show a list (a table, actually) with the orders of the current user.
        orders.setIcon(VaadinIcons.TABLE);
        orders.addStyleName(ValoTheme.MENU_ITEM);
        orders.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        orders.setDescription("Mit rendeltem eddig?");
        orders.addClickListener(e -> {
            setSecondComponent(new OrdersList(getOrderController()));
        });

        //
        logoutButton.setIcon(VaadinIcons.EXIT);
        logoutButton.addStyleName(ValoTheme.MENU_ITEM);
        logoutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        logoutButton.addClickListener(e -> {
            getUI().addWindow(new LoginWindow(customerController, orderController, laborUserController, purificationController, typeController));
            ((OrderhandlerUI) getUI()).setupWelcomeScreen();
        });

        sidebar.addComponents(orderButton, orders, logoutButton);

        return sidebar;
    }

    public CustomerController getCustomerController() {
        return this.customerController;
    }

    public OrderController getOrderController() {
        return this.orderController;
    }

    public LaborUserController getLaborUserController() {
        return this.laborUserController;
    }

    public PurificationController getPurificationController() {
        return this.purificationController;
    }

    public TypeController getTypeController() {
        return this.typeController;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
