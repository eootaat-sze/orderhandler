package com.szbk.View.laborUser;

import com.szbk.Controller.CustomerController;
import com.szbk.Controller.OrderController;
import com.szbk.Model.Entity.Customer;
import com.szbk.Model.Entity.CustomerOrder;
import com.szbk.Model.Entity.LaborUser;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.StringTokenizer;

public class ReportPage extends VerticalLayout {
    private Label siteTitle;
    private ComboBox<String> customerCombobox;
    private Button selectCustomer;
    private HorizontalLayout layout;
    private VerticalLayout mainLayout = new VerticalLayout();
    private Grid<CustomerOrder> customerOrders;
    private Button createReport;

    private LaborUserUI ui;

    private Set<CustomerOrder> selectedOrders;
    private SimpleFileDownloader fileDownloader;

    public ReportPage(LaborUserUI ui) {
        this.ui = ui;

        this.siteTitle = new Label("Report készítés");
        this.customerCombobox = new ComboBox<>();
        this.selectCustomer = new Button("Kiválasztás");
        this.layout = new HorizontalLayout();
        this.customerOrders = new Grid<>(CustomerOrder.class);
        this.createReport = new Button("Export készítés");

        this.fileDownloader = new SimpleFileDownloader();
        addExtension(fileDownloader);

        setupContent();
    }

    private void setupContent() {
        addComponents(siteTitle, layout, customerOrders, createReport);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        setComponentAlignment(layout, Alignment.TOP_CENTER);
        setComponentAlignment(createReport, Alignment.BOTTOM_CENTER);
        setWidth(100, Unit.PERCENTAGE);

        layout.addComponents(customerCombobox, selectCustomer);
        layout.setWidth(70, Unit.PERCENTAGE);
        layout.setExpandRatio(customerCombobox, 70f);

        siteTitle.setStyleName(ValoTheme.LABEL_H2);

        customerCombobox.setPlaceholder("Megrendelő kiválasztása");
        customerCombobox.setItems(ui.getCustomerController().getAllCustomersNameAndEmail());
        customerCombobox.setWidth(100, Unit.PERCENTAGE);
        customerCombobox.setEmptySelectionAllowed(false);

        customerOrders.setSizeFull();
        customerOrders.setVisible(false);

        setupButtons();
    }

    private void setupGrid() {
//        StringTokenizer st = new StringTokenizer(customerCombobox.getSelectedItem().get(), "-");
//        String selectedCustomerEmail = st.nextToken().trim();
//        String selectedCustomerName = st.nextToken().trim();
        String[] data = customerCombobox.getSelectedItem().get().split("-");

        Customer selectedCustomer = ui.getCustomerController().findCustomerByEmail(data[0].trim());

        customerOrders.setItems(ui.getOrderController().getAllOrdersByCustomer(selectedCustomer.getCustomerName(), selectedCustomer.getGroupName(), selectedCustomer.getCompanyName(), "Elkészült"));
        customerOrders.setColumns("sequence", "scale", "purification", "type");
        customerOrders.getColumn("sequence").setCaption("Szekvencia");
        customerOrders.getColumn("purification").setCaption("Tisztítás");
        customerOrders.getColumn("type").setCaption("Típus");
        customerOrders.setVisible(true);
        customerOrders.setSelectionMode(Grid.SelectionMode.MULTI);

        MultiSelect<CustomerOrder> multiSelect = customerOrders.asMultiSelect();

        multiSelect.addSelectionListener(e -> {
            this.selectedOrders = multiSelect.getSelectedItems();
            if (selectedOrders.size() > 0) {
                createReport.setCaption("Report készítés " + selectedOrders.size() + " elemből");

                String filename = data[1].trim().replace(" ", "-").concat("_report");
                setupExportButton(filename);
            } else {
                createReport.setVisible(false);
            }
        });
    }

    private void setupButtons() {
        //Customer select button settings.
        selectCustomer.setStyleName(ValoTheme.BUTTON_PRIMARY);
        selectCustomer.addClickListener(e -> {
            setupGrid();
            changeFilterButtonFunction();
        });

        createReport.setVisible(false);
    }

    private void setupExportButton(String filename) {
        //The export button settings.
        createReport.setVisible(true);
        createReport.setStyleName(ValoTheme.BUTTON_PRIMARY);
        createReport.addClickListener(e -> {
            createAndDownloadFile(filename);
            ui.getOrderController().saveManyOrder(changeOrderStatus());
//            clearFields();
        });
    }

    private void createAndDownloadFile(String filename) {
        StringBuilder builder = new StringBuilder("Szekvencia,Scale,Típus,Tisztítás\n");

        for (CustomerOrder item : selectedOrders) {
            builder.append(item.getSequence());
            builder.append(",");
            builder.append(item.getScale());
            builder.append(",");
            builder.append(item.getType());
            builder.append(",");
            builder.append(item.getPurification());
            builder.append("\n");
        }

        System.out.println("file: " + builder.toString());

        final StreamResource resource = new StreamResource(
            () -> new ByteArrayInputStream(builder.toString().getBytes()), filename + ".csv");

        fileDownloader.setFileDownloadResource(resource);
        fileDownloader.download();
    }

    private Set<CustomerOrder> changeOrderStatus() {
        for (CustomerOrder item : selectedOrders) {
            item.setStatus("Elküldve");
        }

        return selectedOrders;
    }

    //TODO It is strange, that the grid become invisible right after the button click. Some better solution?!
    public void clearFields() {
        customerCombobox.clear();
        customerOrders.setVisible(false);
        createReport.setVisible(false);
    }

    private void changeFilterButtonFunction() {
        if (selectCustomer.getCaption().equals("Kiválasztás")) {
            selectCustomer.setCaption("Új listázás");
            customerCombobox.setEnabled(false);
        } else if (selectCustomer.getCaption().equals("Új listázás")) {
            selectCustomer.setCaption("Kiválasztás");
            customerCombobox.setEnabled(true);
            customerOrders.setVisible(false);
        }
    }
}
