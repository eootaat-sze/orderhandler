package com.szbk.View.laborUser;

import com.szbk.Model.Entity.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ListingPage extends VerticalLayout {
    private LaborUserUI ui;

    private Label siteTitle;
    private ComboBox<String> tableNames;
    private Button selectTable;
    private HorizontalLayout searchLayout;
    private Grid tableContent;

    private String[] tables;

    public ListingPage(LaborUserUI ui) {
        this.ui = ui;
        this.siteTitle = new Label("\"Lássunk mindent\" oldal");
        this.tableNames = new ComboBox<>();
        this.selectTable = new Button("Kiválasztás");
        this.searchLayout = new HorizontalLayout();
//        this.tableContent = new Grid<>();

        this.tables = new String[]{"Megrendelők", "Rendelések", "Laboros felhasználó", "Típus", "Tisztítás"};

        setupContent();
    }

    private void setupContent() {
        siteTitle.setStyleName(ValoTheme.LABEL_H2);

//        addComponents(siteTitle, searchLayout, tableContent);
        addComponents(siteTitle, searchLayout);
        setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        setComponentAlignment(searchLayout, Alignment.TOP_CENTER);
        setWidth(100, Unit.PERCENTAGE);

        tableNames.setItems(tables);
        tableNames.setPlaceholder("Táblanév kiválasztása");
        tableNames.setWidth(100, Unit.PERCENTAGE);
        tableNames.setEmptySelectionAllowed(false);

        selectTable.setStyleName(ValoTheme.BUTTON_PRIMARY);
        selectTable.addClickListener(e -> {
            //TODO Some good function
            if (tableNames.getSelectedItem().isPresent()) {
                handleSelection(tableNames.getSelectedItem().get());
                changeFilterButtonFunction();
            }
        });

        searchLayout.setWidth(70, Unit.PERCENTAGE);
        searchLayout.addComponents(tableNames, selectTable);
        searchLayout.setExpandRatio(tableNames, 70f);

//        tableContent.setWidth(100, Unit.PERCENTAGE);
//        tableContent.setVisible(false);
    }

    private void handleSelection(String selection) {
        switch (selection) {
            case "Megrendelők":
                removeComponentIfIsPresent();
                this.tableContent = new Grid<>(Customer.class);
                tableContent.setWidth(100, Unit.PERCENTAGE);
                //TODO The Vaadin grid should have a generic parameter, but I can't do it.
                //I can't give it a generic parameter, when I create it, because I don't know which entity will be
                //displayed in it. The solution could be that I create the grid here, in place, but with this, I will
                //create a new grid every time I select a new item from the combobox.
                tableContent.setItems(ui.getCustomerController().getAllCustomers());
                tableContent.setColumnOrder("customerName", "email", "companyName", "groupName", "innerName", "password", "id");
                tableContent.getColumn("customerName").setCaption("Megrendelő neve");
                tableContent.getColumn("companyName").setCaption("Cégnév");
                tableContent.getColumn("groupName").setCaption("Csoportnév");
                tableContent.getColumn("innerName").setCaption("Belső név");
                tableContent.getColumn("password").setCaption("Jelszó");
                addComponents(tableContent);
                break;
            case "Rendelések":
                removeComponentIfIsPresent();
                this.tableContent = new Grid<>(CustomerOrder.class);
                tableContent.setWidth(100, Unit.PERCENTAGE);
                tableContent.setItems(ui.getOrderController().getAllOrders());
                tableContent.setColumnOrder("sequence", "type", "purification", "scale", "price", "status", "orderDate", "customerName", "companyName", "groupName", "customerInnerName", "id");
                tableContent.getColumn("sequence").setCaption("Szekvencia");
                tableContent.getColumn("type").setCaption("Típus");
                tableContent.getColumn("purification").setCaption("Tisztítás");
                tableContent.getColumn("price").setCaption("Ár (Forint)");
                tableContent.getColumn("status").setCaption("Státusz");
                tableContent.getColumn("orderDate").setCaption("Megrendelés dátuma");
                tableContent.getColumn("customerName").setCaption("Megrendelő neve");
                tableContent.getColumn("companyName").setCaption("Cégnév");
                tableContent.getColumn("groupName").setCaption("Csoportnév");
                tableContent.getColumn("customerInnerName").setCaption("Megrendelő belső neve");
                addComponents(tableContent);
                break;
            case "Laboros felhasználó":
                removeComponentIfIsPresent();
                this.tableContent = new Grid<>(LaborUser.class);
                tableContent.setWidth(100, Unit.PERCENTAGE);
                tableContent.setItems(ui.getLaborUserController().getAllLaborUsers());
                tableContent.setColumnOrder("name", "email", "password", "id");
                tableContent.getColumn("name").setCaption("Laboros felhasználó neve");
                tableContent.getColumn("password").setCaption("Jelszó");
                addComponents(tableContent);
                break;
            case "Típus":
                removeComponentIfIsPresent();
                this.tableContent = new Grid<>(Type.class);
                tableContent.setWidth(100, Unit.PERCENTAGE);
                tableContent.setItems(ui.getTypeController().getAllTypes());
                tableContent.setColumnOrder("name", "price");
                tableContent.getColumn("name").setCaption("Típus neve");
                tableContent.getColumn("price").setCaption("Ára (Forint)");
                addComponents(tableContent);
                break;
            case "Tisztítás":
                removeComponentIfIsPresent();
                this.tableContent = new Grid<>(Purification.class);
                tableContent.setWidth(100, Unit.PERCENTAGE);
                tableContent.setItems(ui.getPurificationController().getAllPurifications());
                tableContent.setColumnOrder("name", "price");
                tableContent.getColumn("name").setCaption("Tisztítás neve");
                tableContent.getColumn("price").setCaption("Ára (Forint)");
                addComponents(tableContent);
                break;
        }
    }

    private void removeComponentIfIsPresent() {
        if (tableContent != null) {
            removeComponent(tableContent);
        }
    }

    private void changeFilterButtonFunction() {
        if (selectTable.getCaption().equals("Kiválasztás")) {
            selectTable.setCaption("Új szűrés");
            tableNames.setEnabled(false);
        } else if (selectTable.getCaption().equals("Új szűrés")) {
            selectTable.setCaption("Kiválasztás");
            tableNames.setEnabled(true);
            tableContent.setVisible(false);
        }
    }
}
