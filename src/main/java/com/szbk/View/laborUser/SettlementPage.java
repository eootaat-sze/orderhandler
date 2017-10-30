package com.szbk.View.laborUser;

import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

/**
 * Created by dante on 2017.10.17..
 */

public class SettlementPage extends VerticalLayout {
    private Label siteTitle;
    private HorizontalLayout controlLayout;
    private ComboBox<String> companyName;
    private ComboBox<String> groupName;
    private ComboBox<String> customerName;
    private Button selectCustomer;
    private Grid<CustomerOrder> ordersGrid;
    private Button exportDateBtn;

    private LaborUserUI ui;

    private SimpleFileDownloader fileDownloader;
    private List<CustomerOrder> orders;

    public SettlementPage(LaborUserUI ui) {
        this.ui = ui;
        this.fileDownloader = new SimpleFileDownloader();
        this.siteTitle = new Label("Elszámolás készítése");
        this.controlLayout = new HorizontalLayout();
        this.companyName = new ComboBox<>();
        this.groupName = new ComboBox<>();
        this.customerName = new ComboBox<>();
        this.selectCustomer = new Button("Listázás");
        this.ordersGrid = new Grid<>(CustomerOrder.class);
        this.exportDateBtn = new Button("CSV exportálás");

        addExtension(fileDownloader);
        setupContent();
    }

    private void setupContent() {
        siteTitle.setStyleName(ValoTheme.LABEL_H2);
        companyName.setPlaceholder("Cégnév");
        companyName.setWidth(100, Unit.PERCENTAGE);
        companyName.setItems(ui.getOrderController().getCompanyNames());
        companyName.setEmptySelectionAllowed(false);
        companyName.addSelectionListener(e -> {
//            System.out.println("selection");
            if (companyName.getSelectedItem().isPresent()) {
                String comName = companyName.getSelectedItem().get();
                if (!comName.equals("")) {
                    List<String> groupNames = ui.getOrderController().getGroupNames(comName);
                    if (groupNames != null) {
                        groupName.clear();
                        groupName.setItems(groupNames);
                        groupName.setEnabled(true);
                    }
                }
            }
        });

        groupName.setPlaceholder("Csoportnév");
        groupName.setWidth(100, Unit.PERCENTAGE);
        groupName.setEnabled(false);
        groupName.setEmptySelectionAllowed(false);
        groupName.addSelectionListener(e -> {
//            System.out.println("gSelection");
            if (companyName.getSelectedItem().isPresent() && groupName.getSelectedItem().isPresent()) {
                String comName = companyName.getSelectedItem().get();
                String gName = groupName.getSelectedItem().get();
                if (!comName.equals("") && !gName.equals("")) {
                    List<String> customerNames = ui.getOrderController().getCustomerNames(comName, gName);
                    if (customerNames != null) {
                        customerName.setItems(customerNames);
                        customerName.setEnabled(true);
                    }
                }
            }
        });

        groupName.addValueChangeListener(e -> {
            System.out.println("value");
            customerName.clear();
        });

        customerName.setPlaceholder("Megrendelő neve");
        customerName.setWidth(100, Unit.PERCENTAGE);
        customerName.setEnabled(false);
        customerName.setEmptySelectionAllowed(false);

        selectCustomer.setStyleName(ValoTheme.BUTTON_PRIMARY);
        selectCustomer.addClickListener(e -> {
            changeFilterButtonFunction();
        });

        ordersGrid.setSizeFull();
        ordersGrid.setVisible(false);

        exportDateBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        exportDateBtn.addClickListener(e -> {
            createAndDownloadFile(createFilename());
            ui.getOrderController().saveManyOrder(changeOrderStatus());
        });
        exportDateBtn.setVisible(false);

        controlLayout.addComponents(companyName, groupName, customerName, selectCustomer);
        controlLayout.setWidth(100, Unit.PERCENTAGE);
        controlLayout.setExpandRatio(companyName, 30f);
        controlLayout.setExpandRatio(groupName, 30f);
        controlLayout.setExpandRatio(customerName, 30f);
        addComponents(siteTitle, controlLayout, ordersGrid, exportDateBtn);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        setComponentAlignment(exportDateBtn, Alignment.BOTTOM_CENTER);
    }

    private void changeFilterButtonFunction() {
        if (selectCustomer.getCaption().equals("Listázás")) {
            selectCustomer.setCaption("Új szűrés");
            companyName.setEnabled(false);
            groupName.setEnabled(false);
            customerName.setEnabled(false);
            setGridData();
        } else if (selectCustomer.getCaption().equals("Új szűrés")) {
            selectCustomer.setCaption("Listázás");
            ordersGrid.setVisible(false);
            exportDateBtn.setVisible(false);
            companyName.setEnabled(true);
            groupName.setEnabled(true);
            customerName.setEnabled(true);
        }
    }

    private void setupGrid(List<CustomerOrder> orders) {
        ordersGrid.setItems(orders);
        ordersGrid.setColumns("customerName", "price", "status");
        ordersGrid.getColumn("customerName").setCaption("Megrendelő neve");
        ordersGrid.getColumn("price").setCaption("Ár (Forint)");
        ordersGrid.getColumn("status").setCaption("Státusz");
        ordersGrid.setVisible(true);
    }

    private void setGridData() {
        String comName = companyName.getSelectedItem().get();
        String gName = groupName.getSelectedItem().get();
        String cName = customerName.getSelectedItem().get();
        if (!comName.equals("") && !gName.equals("") && !cName.equals("")) {
//                List<CustomerOrder> orders = ui.getOrderController().getOrdersByCompanyGroupAndCustomerName(comName, gName, cName);
            orders = ui.getOrderController().getAllOrdersByCustomer(cName, gName, comName, "Elküldve");
            if (orders != null) {
//                changeFilterButtonFunction();
                setupGrid(orders);
                exportDateBtn.setVisible(true);
            } else {
                Notification noItemsNotification = new Notification("Nincs a szűrésnek megfelelő megrendelés!");
                noItemsNotification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                noItemsNotification.setPosition(Position.TOP_CENTER);
                noItemsNotification.show(getUI().getPage());
            }
        }
    }

    private void createAndDownloadFile(String filename) {
        StringBuilder builder = new StringBuilder("Megrendelő neve,Ár\n");

        for (CustomerOrder item : this.orders) {
            builder.append(item.getCustomerName());
            builder.append(",");
            builder.append(item.getPrice());
            builder.append("\n");
        }

        System.out.println("file: " + builder.toString());

        final StreamResource resource = new StreamResource(
                () -> new ByteArrayInputStream(builder.toString().getBytes()), filename + ".csv");

        fileDownloader.setFileDownloadResource(resource);
        fileDownloader.download();
    }

    private String createFilename() {
        StringBuilder builder = new StringBuilder();
        builder.append(getOutTheSpaces(companyName.getSelectedItem().get()));
        builder.append("-");
        builder.append(getOutTheSpaces(groupName.getSelectedItem().get()));
        builder.append("-");
        builder.append(getOutTheSpaces(customerName.getSelectedItem().get()));
        builder.append("_szamlazas");

        return builder.toString();
    }

    private String getOutTheSpaces(String name) {
        if (name.contains(" ")) {
            String[] items = name.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String item : items) {
                builder.append(item);
            }

            return builder.toString();
        }

        return name;
    }

    private List<CustomerOrder> changeOrderStatus() {
        for (CustomerOrder item : orders) {
            item.setStatus("Számlázva");
        }

        return this.orders;
    }

    public void clearFields() {
        companyName.clear();
        groupName.clear();
        customerName.clear();
        selectCustomer.setCaption("Listázás");
        ordersGrid.setVisible(false);
        exportDateBtn.setVisible(false);
    }
}
