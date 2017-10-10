package com.szbk.View.customer;

import com.szbk.Controller.OrderController;
import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class OrdersList extends VerticalLayout {
    private Label siteTitle;
    private Grid ordersGrid;

    private OrderController controller;

    public OrdersList(OrderController controller) {
        this.controller = controller;

        siteTitle = new Label("Az eddigi rendeléseket a lenti táblázatban találod");
        siteTitle.setStyleName(ValoTheme.LABEL_H2);

        ordersGrid = new Grid<>(CustomerOrder.class);
        setupGrid();

        addComponents(siteTitle, ordersGrid);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
    }

    private void setupGrid() {
        ordersGrid.setSizeFull();
        List<CustomerOrder> orders = controller.getAllOrdersByCustomer(
                String.valueOf(VaadinSession.getCurrent().getAttribute("customerName")),
                String.valueOf(VaadinSession.getCurrent().getAttribute("groupName")),
                String.valueOf(VaadinSession.getCurrent().getAttribute("companyName")));
        ordersGrid.setItems(orders);
        ordersGrid.setColumns("orderDate", "status", "sequence", "scale", "purification", "type");
        ordersGrid.getColumn("orderDate").setCaption("Megrendelés dátuma");
        ordersGrid.getColumn("status").setCaption("Státusz");
        ordersGrid.getColumn("sequence").setCaption("Szekvencia");
        ordersGrid.getColumn("purification").setCaption("Tisztítás");
        ordersGrid.getColumn("type").setCaption("Típus");
    }
}
