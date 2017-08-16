package com.szbk.View.laborUser;

import com.szbk.Controller.OrderController;
import com.szbk.Model.Entity.Customer;
import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class AllCustomerOrders extends VerticalLayout {
    private Label siteTitle;
    private Label description;
    private Grid<CustomerOrder> orders;
    private Button jobButton;

    private OrderController orderController;

    //Temporary label
    private Label gridRowNumber;

    public AllCustomerOrders(OrderController controller) {
        orderController = controller;

        siteTitle = new Label("Az összes elküldött megrendelést a lenti táblázatban találod");
        siteTitle.setStyleName(ValoTheme.LABEL_H2);

        description = new Label("A bal oldali checkbox-szal tudsz megrendeléseket job-ba tenni (max. 24)");

        orders = new Grid<>(CustomerOrder.class);
        jobButton = new Button();

        gridRowNumber = new Label();

        setupGridAndButton();

        addComponents(siteTitle, description, gridRowNumber, orders, jobButton);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        setComponentAlignment(description, Alignment.TOP_CENTER);
        setComponentAlignment(gridRowNumber, Alignment.TOP_CENTER);
        setComponentAlignment(jobButton, Alignment.BOTTOM_CENTER);
        setExpandRatio(orders, 1);
        setSizeFull();
    }

    private void setupGridAndButton() {
        orders.setSizeFull();

        List<CustomerOrder> allOrders = orderController.getAllOrders();
        orders.setItems(allOrders);
        orders.setColumns("customerName", "sequence", "scale", "purification", "type", "orderDate");
        orders.setSelectionMode(Grid.SelectionMode.MULTI);

        gridRowNumber.setCaption("Sorok száma: " + allOrders.size());

        //Try adding a field to edit the sequence.
        TextField sequenceEdit = new TextField();
        Binder<CustomerOrder> dataBinder = orders.getEditor().getBinder();

        Binder.Binding<CustomerOrder, String> sequenceBinding =
                dataBinder.bind(sequenceEdit, CustomerOrder::getSequence, CustomerOrder::setSequence);

        //FIXME ???
        orders.getColumn("sequence").setEditorComponent(sequenceEdit);
        orders.getEditor().addSaveListener(e -> {
            CustomerOrder order = e.getBean();
            System.out.println("order: " + order);
            orderController.saveOrder(order);
        });

        orders.getEditor().setEnabled(true);

        MultiSelect multiSelect = orders.asMultiSelect();
        multiSelect.addSelectionListener(e -> {
            if (multiSelect.getSelectedItems().size() > 0 && multiSelect.getSelectedItems().size() <= 24) {
//                Notification.show("2 items were selected!");
                jobButton.setVisible(true);
                jobButton.setCaption("Job készítés " + multiSelect.getSelectedItems().size() + " elemből");
            } else {
                jobButton.setVisible(false);
            }
        });

        jobButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        jobButton.setVisible(false);
        jobButton.addClickListener(e -> {
            getUI().addWindow(new JobWindow(multiSelect.getSelectedItems()));
        });
    }


}
