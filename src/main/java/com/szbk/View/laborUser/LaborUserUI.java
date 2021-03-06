package com.szbk.View.laborUser;

import com.szbk.Controller.*;
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
 * Created by dante on 2017.07.31..
 */
public class LaborUserUI extends HorizontalSplitPanel implements View {
    private OrderController orderController;
    private CustomerController customerController;
    private LaborUserController laborUserController;
    private PurificationController purificationController;
    private TypeController typeController;

    private AllCustomerOrders allOrders;
    private ReportPage reportPage;
    private SettlementPage settlementPage;
    private ListingPage listingPage;

    public LaborUserUI(OrderController oc, CustomerController cc, LaborUserController lc, PurificationController pc, TypeController tc) {
        this.orderController = oc;
        this.customerController = cc;
        this.laborUserController = lc;
        this.purificationController = pc;
        this.typeController = tc;

        this.allOrders = new AllCustomerOrders(this);
        this.reportPage = new ReportPage(this);
        this.settlementPage = new SettlementPage(this);
        this.listingPage = new ListingPage(this);

        setSplitPosition(18f, Unit.PERCENTAGE);
        setLocked(true);

        setFirstComponent(setupSideBarMenu());
        setSecondComponent(setupDescriptionText());
    }
    
    private VerticalLayout setupDescriptionText() {
        VerticalLayout descriptionTextLayout = new VerticalLayout();
        //TODO Maybe I could work a bit on the text, to be aligned better. Not a big problem though.
        String descriptionText = "Főoldal!<br/>" +
                "Sikeresen beléptél, gratulálok!<br/>" +
                "Laborosként a bal oldalon felsorolt lehetőségek állnak a rendelkezésedre. <br/>" +
                "Tudod kezelni a megrendeléseket, munkafolyamatba helyezni őket, report készítést<br/>" +
                "elindítani, elszámolást végezni. Illetve egy áttekintő nézet is megtalálható, ahol<br/>" +
                "mindent meg tudsz nézni. Ezen kívül természetesen kijelentkezni tudsz még. <br/>" +
                "Csak hogy felkészítselek, mi vár rád, ha bármelyikre ráklikkelsz:<br/>" +
                "<ul>" +
                "<li><strong>\"Miket rendeltek?\"</strong>: a beérkezdett megrendelésekkel kapcsolatos információkat láthatod itt. <br/>" +
                "Ezen kívül itt rendezhetőek munkafolyamatokba a megrendeléseket, illetve a szekvenciák szerkeszthetőek is.</li>" +
                "<li><strong>\"Report készítés\"</strong>: egy Excel fájl generálható adott megrendelőhöz tartozó megrendelésekből. <br/>" +
                "A megrendelő lenyíló listából választható ki.</li>" +
                "<li><strong>\"Elszámolás\"</strong>: itt is a lenyíló lista játssza a főszerepet, egyszerre mindjárt három.<br/>" +
                "Cég, azon belül csoport, azon belül személy választandó ki, vagy egy csoporthoz tartozó mindenki.<br/>" +
                "Ebben az esetben is Excel fájl generálható.</li>" +
                "<li><strong>\"Lássunk mindent\"</strong>: itt az adatbázisban található összes releváns adat kilistázásra kerül, <br/>" +
                "valamilyen formában.</li>" +
                "<li><strong>\"Kijelentkezés\"</strong>: amennyiben végeztél, itt tudsz kijelentkezni (milyen meglepő, ugye?)<br/>" +
                "De akár egyszerűen csak be is zárhatod az oldalt.</li>" +
                "</ul>";

        Label descriptionTextLabel = new Label(descriptionText);
        descriptionTextLabel.setContentMode(ContentMode.HTML);
        descriptionTextLayout.addComponent(descriptionTextLabel);
        descriptionTextLayout.setComponentAlignment(descriptionTextLabel, Alignment.MIDDLE_CENTER);

        return descriptionTextLayout;
    }

    private SideBarMenu setupSideBarMenu() {
        String menuTitle = "Hello, " + VaadinSession.getCurrent().getAttribute("laborUserName") + "!";

        SideBarMenu sidebar = new SideBarMenu(menuTitle);
        Button ordersButton = new Button("Miket rendeltek eddig?");
        Button reportButton = new Button("Report készítés");
        Button whateverButton = new Button("Elszámolás");
        Button listOfEverythingButton = new Button("Lássunk mindent!");
        Button logoutButton = new Button("Kilépés");

        ordersButton.setIcon(VaadinIcons.TABLE);
        ordersButton.addStyleName(ValoTheme.MENU_ITEM);
        ordersButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        ordersButton.addClickListener(e -> {
//            Notification.show("Még idő, de majd láthatod, mit rendeltek");
            allOrders.refreshOrders();
            setSecondComponent(allOrders);
        });

        reportButton.setIcon(VaadinIcons.PRINT);
        reportButton.addStyleName(ValoTheme.MENU_ITEM);
        reportButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        reportButton.addClickListener(e -> {
//            Notification.show("Report? Miért, feedel?");
            reportPage.clearFields();
            setSecondComponent(reportPage);
        });

        whateverButton.setIcon(VaadinIcons.CREDIT_CARD);
        whateverButton.addStyleName(ValoTheme.MENU_ITEM);
        whateverButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        whateverButton.addClickListener(e -> {
//            Notification.show("Ez az elszámolásért felelős funkció... amúgy.");
//            settlementPage.clearFields();
            setSecondComponent(settlementPage);
        });

        listOfEverythingButton.setIcon(VaadinIcons.ARCHIVE);
        listOfEverythingButton.addStyleName(ValoTheme.MENU_ITEM);
        listOfEverythingButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        listOfEverythingButton.addClickListener(e -> {
//            Notification.show("Itt majd mindent kilistázok ám.");
            setSecondComponent(listingPage);
        });

        logoutButton.setIcon(VaadinIcons.EXIT);
        logoutButton.addStyleName(ValoTheme.MENU_ITEM);
        logoutButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        logoutButton.addClickListener(e -> {
//            Notification.show("Itt majd ki tudsz lépni, keep calm.");
            getUI().addWindow(new LoginWindow(customerController, orderController, laborUserController, purificationController, typeController));
            ((OrderhandlerUI) getUI()).setupWelcomeScreen();
        });

        sidebar.addComponents(ordersButton, reportButton, whateverButton, listOfEverythingButton, logoutButton);
//        sideBarMenu.addComponent(menuLayout);
        return sidebar;
    }

    public OrderController getOrderController() {
        return orderController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public LaborUserController getLaborUserController() {
        return laborUserController;
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
