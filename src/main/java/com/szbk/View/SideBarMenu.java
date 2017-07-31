package com.szbk.View;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by dante on 2017.04.11..
 */
public class SideBarMenu extends CssLayout {
    Label title;

    public SideBarMenu(String titleText) {
        this.title = new Label(titleText);
        this.title.setStyleName(ValoTheme.MENU_TITLE);

        addStyleName(ValoTheme.MENU_ROOT);
        setHeight("100%");
        setWidth("100%");

        addComponents(title);
    }

//    Button orderButton;
//    Button orders;
//    Button logout;
//    VerticalLayout layout;

//    public SideBarMenu(String titleText) {
//        layout = new VerticalLayout();
//        layout.setMargin(false);
//        title = new Label(titleText);
//        title.addStyleName(ValoTheme.MENU_TITLE);
//
//        orderButton = new Button("Rendelek valamit");
//        orderButton.setIcon(VaadinIcons.SHOP);
//        orderButton.addStyleName(ValoTheme.MENU_ITEM);
//        orderButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//        orderButton.addClickListener(e -> {
////            Notification.show(orderButton.getCaption());
//            customerUI.setSecondComponent(new OrderWindow(customerUI.getOrderController()));
//        });
//
//        orders = new Button("Mit rendeltem eddig?");
//        orders.setIcon(VaadinIcons.TABLE);
//        orders.addStyleName(ValoTheme.MENU_ITEM);
//        orders.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//        orders.setDescription("Mit rendeltem eddig?");
//        orders.addClickListener(e -> {
////            Notification.show(orders.getCaption());
//            customerUI.setSecondComponent(new OrdersList(customerUI.getOrderController()));
//        });
//
//        logout = new Button("Kilépés");
//        logout.setIcon(VaadinIcons.EXIT);
//        logout.addStyleName(ValoTheme.MENU_ITEM);
//        logout.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//        logout.addClickListener(e -> {
//            getUI().addWindow(new LoginWindow(customerUI.getCustomerController(), customerUI.getOrderController()));
//            ((OrderhandlerUI) getUI()).setupWelcomeScreen();
//        });
//
////        layout.addComponents(orderButton, orders, logout);
//        layout.addComponents(orderButton, orders, logout);
//        addStyleName(ValoTheme.MENU_ROOT);
//        addComponents(title, layout);
//        addComponent(title);
//        setHeight("100%");
//        setWidth("100%");
//    }
}
