package com.szbk.View.laborUser;

import com.szbk.Controller.OrderController;
import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class AllCustomerOrders extends VerticalLayout {
    private Label siteTitle;
    private Grid orders;

    private OrderController orderController;

    public AllCustomerOrders(OrderController controller) {
        this.orderController = controller;

        this.siteTitle = new Label("Az összes elküldött megrendelést a lenti táblázatban találod");
        this.siteTitle.setStyleName(ValoTheme.LABEL_H2);

        this.orders = new Grid<>(CustomerOrder.class);
        setupGrid();

        addComponents(siteTitle, orders);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
    }

    private void setupGrid() {
        orders.setSizeFull();

        List<CustomerOrder> allOrders = orderController.getAllOrders();
        orders.setItems(allOrders);
        orders.setColumns("customerName", "sequence", "scale", "purification", "type", "orderDate");
//        orders.addColumn("Job");

        orders.setSelectionMode(Grid.SelectionMode.MULTI);
        MultiSelect multiSelect = orders.asMultiSelect();
        multiSelect.addSelectionListener(e -> {
            //FIXME So the question: there can be 20 items max for a job, but that number must be 20, or it can be 20 or less?
            if (multiSelect.getSelectedItems().size() == 2) {
                Notification.show("2 items were selected!");
                getUI().addWindow(new JobWindow(multiSelect.getSelectedItems()));
            }
        });
    }


}
