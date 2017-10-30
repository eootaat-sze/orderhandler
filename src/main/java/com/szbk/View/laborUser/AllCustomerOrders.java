package com.szbk.View.laborUser;

import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.Set;

public class AllCustomerOrders extends VerticalLayout {
    private Label siteTitle;
    private Label description;
    private Grid<CustomerOrder> orders;
    private Button jobButton;

    private LaborUserUI ui;
    private JobWindow jobWindow;

    List<CustomerOrder> allOrders;

    public AllCustomerOrders(LaborUserUI ui) {
        this.ui = ui;

        siteTitle = new Label("Az összes elküldött megrendelést a lenti táblázatban találod");
        siteTitle.setStyleName(ValoTheme.LABEL_H2);

        description = new Label("A bal oldali checkbox-szal tudsz megrendeléseket job-ba tenni (max. 24)");

        orders = new Grid<>(CustomerOrder.class);
        jobButton = new Button();

        setupContent();

//        addComponents(siteTitle, description, gridRowNumber, orders, jobButton);
        addComponents(siteTitle, description, orders, jobButton);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        setComponentAlignment(description, Alignment.TOP_CENTER);
//        setComponentAlignment(gridRowNumber, Alignment.TOP_CENTER);
        setComponentAlignment(jobButton, Alignment.BOTTOM_CENTER);
        setExpandRatio(orders, 1);
        setSizeFull();
    }

    private void setupContent() {
        orders.setSizeFull();

//        List<CustomerOrder> allOrders = orderController.getAllOrders();
//        orders.setItems(allOrders);
        refreshOrders();
        orders.setColumns("customerName", "sequence", "scale", "purification", "type", "status", "orderDate");
        orders.setSelectionMode(Grid.SelectionMode.MULTI);

        //Set the column captions.
        orders.getColumn("customerName").setCaption("Megrendelő neve");
        orders.getColumn("sequence").setCaption("Szekvencia");
        orders.getColumn("purification").setCaption("Tisztítás");
        orders.getColumn("type").setCaption("Típus");
        orders.getColumn("status").setCaption("Státusz");
        orders.getColumn("orderDate").setCaption("Rendelési dátum");

        //Try adding a field to edit the sequence.
        TextField sequenceEdit = new TextField();
        Binder<CustomerOrder> dataBinder = orders.getEditor().getBinder();

//        Binder.Binding<CustomerOrder, String> sequenceBinding =
//                dataBinder.bind(sequenceEdit, CustomerOrder::getSequence, CustomerOrder::setSequence);
        Binder.Binding<CustomerOrder, String> sequenceBinding =
                dataBinder.forField(sequenceEdit).asRequired("A szekvencia mező nem lehet üres!")
                    .withValidator((Validator<String>) (value, context) -> {
                        if (sequenceEdit.getValue().contains(" ")) {
                            return ValidationResult.error("A szekvencia nem tartalmazhat szóközt!");
                        } else {
                            return ValidationResult.ok();
                        }
                    }).bind(CustomerOrder::getSequence, CustomerOrder::setSequence);

        orders.getColumn("sequence").setEditorComponent(sequenceEdit);
        orders.getEditor().addSaveListener(e -> {
            CustomerOrder order = e.getBean();
            System.out.println("order: " + order);
            ui.getOrderController().saveOrder(order);
        });

        orders.getEditor().setEnabled(true);

        MultiSelect<CustomerOrder> multiSelect = orders.asMultiSelect();

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
            Set<CustomerOrder> items = multiSelect.getSelectedItems();

            if (jobWindow == null) {
                jobWindow = new JobWindow(ui, items);
                jobWindow.addCloseListener(event -> {
                    refreshOrders();
                });
            }
            jobWindow.setOrdersForJob(items);
            getUI().addWindow(jobWindow);
//            orders.deselectAll();
        });
    }

    void refreshOrders() {
        allOrders = ui.getOrderController().getAllOrders();
        orders.setItems(allOrders);
    }

}
